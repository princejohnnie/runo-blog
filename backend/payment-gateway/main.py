from fastapi import FastAPI, status
from fastapi.responses import JSONResponse
from pydantic import BaseModel
import datetime
from datetime import datetime
import json
 
month = datetime.now().month
year = datetime.now().year

from rave_python import Rave, RaveExceptions, Misc
rave = Rave("FLWPUBK_TEST-78a61758e2d90571d80bf53541cc1eb8-X", "FLWSECK_TEST-96cb2ad54d53ab329293d23b0f0a882e-X", usingEnv = False)

app = FastAPI()

# example usage:
# curl localhost:8042/execute-payment -X POST -H 'Content-Type: application/json' -d '{"card_holder": "JOHN DOE", "card_number": "4242 4242 4242 4242", "price": 100}'


class PaymentDetails(BaseModel):
    card_holder: str
    card_number: str
    card_cvv: str
    card_expiry_month: int
    card_expiry_year: int
    card_pin: str
    email: str
    phone: str
    amount: int
    price: float | None = None


CardType = str

mockPayload = {
  "cardno": "4751763236699647",
  "cvv": "470",
  "expirymonth": "09",
  "expiryyear": "35",
  'pin': "3310",
  "amount": 10,
  "email": "princechinecherem@gmail.com",
  "phonenumber": "08109514619",
  "firstname": "temi",
  "lastname": "desola",
}


@app.post("/execute-payment")
async def execute_payment(payment_details: PaymentDetails):
    try:
        card_type = validate_card_number(payment_details)
        validate_cvv(payment_details, card_type)
        validate_card_expiry(payment_details)

        return process_payment(payment_details)
    
    except Exception as e:
        return JSONResponse({"message": str(e)}, status.HTTP_422_UNPROCESSABLE_ENTITY)


def process_payment(payment_details: PaymentDetails) -> dict:
    firstName = payment_details.card_holder.split()[0]
    lastName = payment_details.card_holder.split()[1]

    expiryMonth = str(payment_details.card_expiry_month)
    expiryYear = str(payment_details.card_expiry_year)

    print("expirymonth -> ", expiryMonth)
    print("expirymonth -> ", expiryYear)

    payload = {
        "cardno": payment_details.card_number,
        "cvv": payment_details.card_cvv,
        "expirymonth": expiryMonth,
        "expiryyear": expiryYear,
        "pin": payment_details.card_pin,
        "email": payment_details.email,
        "firstname": firstName,
        "lastname": lastName,
        "phonenumber": payment_details.phone,
        "amount": payment_details.amount

    }

    try:
        res = rave.Card.charge(payload)

        if res["validationRequired"]:
            print("Validating Payment!")
            rave.Card.validate(res["flwRef"], "12345")

        transactionStatus = rave.Card.verify(res["txRef"])

        print("Transaction Status -> ", transactionStatus["transactionComplete"])

        return JSONResponse(res, status_code=status.HTTP_200_OK)

    except RaveExceptions.CardChargeError as e:
        print(e.err["errMsg"])
        print(e.err["flwRef"])

    except RaveExceptions.TransactionValidationError as e:
        print(e.err)
        print(e.err["flwRef"])

    except RaveExceptions.TransactionVerificationError as e:
        print(e.err["errMsg"])
        print(e.err["txRef"])


    return JSONResponse(e.err, status_code=status.HTTP_404_NOT_FOUND)


def validate_card_number(payment_details: PaymentDetails) -> CardType:
    normalized_card_number = payment_details.card_number.replace(" ", "")

    if not normalized_card_number:
        raise ValueError("Card number must be supplied")

    if not all(map(str.isdigit, normalized_card_number)):
        raise ValueError("Card number must consist only of numbers")

    if len(normalized_card_number) != 16:
        raise ValueError("Card number must be exactly 16 digits")

    def luhn_algorithm_valid(number: str) -> bool:
        def digits_of(n: str):
            return [int(d) for d in n]

        digits = digits_of(number)
        odd_digits = digits[-1::-2]
        even_digits = digits[-2::-2]
        checksum = sum(odd_digits, 0)
        for d in even_digits:
            checksum += sum(digits_of(str(d * 2)))

        return checksum % 10 == 0

    if not luhn_algorithm_valid(normalized_card_number):
        raise ValueError("The supplied card number is not valid")

    if normalized_card_number.startswith("4"):
        return "visa"
    elif normalized_card_number.startswith("35") or normalized_card_number.startswith("37"):
        return "mastercard"
    else:
        raise ValueError("Unsupported card type")


def validate_cvv(payment_details: PaymentDetails, card_type: CardType) -> None:
    normalized_cvv = payment_details.card_cvv.replace(" ", "")

    if not normalized_cvv:
        raise ValueError("Card number must be supplied")

    if not all(map(str.isdigit, normalized_cvv)):
        raise ValueError("Card CVV must consist only of numbers")

    if card_type == "mastercard" and len(normalized_cvv) != 4:
        raise ValueError("Mastercard CVV should have exactly 4 digits")

    if len(normalized_cvv) != 3:
        raise ValueError("CVV should have exactly 3 digits")
    
    
def validate_card_expiry(payment_details: PaymentDetails) -> None:
    month = payment_details.card_expiry_month
    year = payment_details.card_expiry_year
    print("Expiry Month -> ", month)
    print("Expiry Year -> ", year)

    currentMonth = datetime.now().month
    currentYear = datetime.now().year

    print("currentMonth -> ", currentMonth)
    print("currentYear -> ", currentYear)

    if currentYear > year:
        raise ValueError("Invalid expiry date")
    
    if currentYear == year:
        if currentMonth > month:
            raise ValueError("Invalid expiry date")
        
