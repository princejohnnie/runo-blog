<script setup>
import Modal from '@/components/modals/Modal.vue';
import Swal from 'sweetalert2';
import Input from '@/components//form/Input.vue';
import Form from '@/components/form/Form.vue';
import Button from '@/components/form/Button.vue';
import Auth from '@/requests/Auth.js';
import VueDatePicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css';

import { ref, computed } from 'vue';
import { useUserStore } from '@/stores/user.js'


const userStore = useUserStore()

const emit = defineEmits(['closeModal'])

const isProcessing = ref(false)
const monthlyCost = ref(20);
const subscriptionData = ref({
    cardNumber: '',
    cardCvv: '',
    cardExpiryDate: '',
    name: '',
    surname: '',
    address: '',
    subscriptionType: 'monthly',
    email: '',

});

//TODO define submit logic when backend endpoint exists
const submitForm = async () => {
    //const response = await Auth.register(data.value)
    //localStorage.setItem('token', response.data);

    //await userStore.me()

    showSuccessAlert()

}

const showSuccessAlert = () => {
    Swal.fire({
        title: "Success!",
        text: "You have been registered successfully!",
        icon: "success"
    });
}

const subscriptionTotalCost = computed(() => {
    if (subscriptionData.value.subscriptionType === 'monthly') {
        return monthlyCost.value;
    } else {
        return (monthlyCost.value * 0.75) * 12;
    }
})

const subscriptionMonthlyCost = computed(() => {
    if (subscriptionData.value.subscriptionType === 'monthly') {
        return monthlyCost.value;
    } else {
        return monthlyCost.value * 0.75;
    }
})

</script>

<template>
    <Modal @closeModal="onCloseModal()">
        <div class="modal__form">
            <h2 class="modal__heading">Choose plan</h2>

            <Form :handleLogic="submitForm" v-model:is-processing="isProcessing">
                <div class="editSubscription__Types">
                    <div class="editSubscription__Type">
                        <input class="editSubscription__radio" type="radio" id="monthly" value="monthly" checked
                            v-model="subscriptionData.subscriptionType" />
                        <label class="editSubscription__typeLabel" for="monthly">Monthly</label>
                    </div>
                    <div class="editSubscription__Type">
                        <input class="editSubscription__radio" type="radio" id="yearly" value="yearly"
                            v-model="subscriptionData.subscriptionType" />
                        <label class="editSubscription__typeLabel" for="yearly">Yearly</label>
                    </div>
                </div>
                <div class="editSubscription__costs">
                    <div class="editSubscription__cost">
                        <highlight>{{ subscriptionMonthlyCost }}€</highlight> per month
                    </div>
                    <div class="editSubscription__cost">Total: <highlight>{{ subscriptionTotalCost }}€</highlight>
                    </div>
                </div>
                <Input type="text" name="number" label="Card number" placeholder=""
                    v-model:value="subscriptionData.cardNumber" />
                <div class="editSubscription__gridOptions">
                    <Input type="text" name="cvv" label="CVV" placeholder="" v-model:value="subscriptionData.cardCvv"
                        style="width:100%" />

                    <div class="editSubscription__gridOption">
                        <label class="modal__inputLabel">Expiry date</label>
                        <VueDatePicker class="editSubscription__date" v-model="subscriptionData.cardExpiryDate" monthPicker
                            required hideInputIcon placeholder="12/2023"></VueDatePicker>
                    </div>
                </div>
                <Input type="text" name="name" label="Name" placeholder="Your name" v-model:value="subscriptionData.name" />
                <Input type="text" name="surname" label="Surname" placeholder="Your surname"
                    v-model:value="subscriptionData.surname" />
                <Input type="text" name="address" label="Address" placeholder="" v-model:value="subscriptionData.address" />
                <Input type="email" name="email" label="Email" placeholder="" v-model:value="subscriptionData.email" />

                <div class="editSubscription__inputWrapper">
                    <Button type="submit" class="editProfile__inputButton" :isProcessing="isProcessing">Go Premium</Button>
                </div>
            </Form>
        </div>
    </Modal>
</template>
