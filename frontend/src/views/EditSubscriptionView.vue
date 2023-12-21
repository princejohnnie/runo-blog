<script setup>
import { ref, computed, watch } from 'vue';
import { useUserStore } from '@/stores/user.js';
import { useRouter } from 'vue-router';
import ProfileHeader from "@/components/profile/ProfileHeader.vue";
import Input from '@/components/form/Input.vue';
import Form from '@/components/form/Form.vue';
import Button from '@/components/form/Button.vue';
import VueDatePicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css';
import ChevronDownIcon from '@/components/icons/ChevronDownIcon.vue'
import Swal from 'sweetalert2';

const router = useRouter()
const userStore = useUserStore();
const links = [
    {
        text: "Back to profile",
        to: { name: "my-profile" }
    }
]


const subscriptionData = ref({
    cardNumber: '',
    cardCvv: '',
    cardPin: '',
    cardExpiryDate: '',
    cardHolder: '',
    address: '',
    subscriptionType: 'monthly',
    email: '',
    phone: ''

});

const isVisible = ref(false);
const isProcessing = ref(false)
const history = ref([]);
const monthlyCost = ref(20);

userStore.subscriptions().then((res) => {
    console.log(res.data);
    history.value = res.data;
})

const toggleTable = () => {
    isVisible.value = !isVisible.value;
};


const showSuccessAlert = () => {
    Swal.fire({
        title: "Success!",
        text: "You have logged in successfully!",
        icon: "success"
    });
}

const submitForm = async () => {
    subscriptionData.value.cardExpiryDate.month += 1;
    const response = await userStore.subscribe(subscriptionData.value);
    await userStore.me()
    router.push({ name: 'my-profile' })
};

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

const subscriptionDate = (transaction) => {
    const relevantDate = computed(() => {
        const dateObject = new Date(transaction);
        const day = dateObject.getDate();
        const month = dateObject.getMonth() + 1;
        const year = dateObject.getFullYear();
        return `${day}.${month}.${year}`;
    });
    return relevantDate.value;
};

const subscriptionStatus = (transaction) => {
    const relevantStatus = computed(() => {
        return transaction.charAt(0).toUpperCase() + transaction.slice(1);
    });
    return relevantStatus.value;
};

const hasHistory = computed(() => {
    return history.value.length ? true : false;
});

</script>
<template>
    <ProfileHeader :links="links" />
    <div class="section section-editSubscription">
        <div class="editSubscription__form">
            <h2 class="editSubscription__heading">Edit subscription</h2>
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
                    v-model:value="subscriptionData.cardNumber" required />
                <div class="editSubscription__gridOptions">
                    <Input type="text" name="cvv" label="CVV" placeholder="" v-model:value="subscriptionData.cardCvv"
                        style="width:100%" required />

                    <Input type="text" name="pin" label="Pin" placeholder="" v-model:value="subscriptionData.cardPin"
                        style="width:100%" />

                    <div class="editSubscription__gridOption">
                        <label class="modal__inputLabel">Expiry date</label>
                        <VueDatePicker class="editSubscription__date" v-model="subscriptionData.cardExpiryDate" monthPicker
                            required hideInputIcon placeholder="12/2023"></VueDatePicker>
                    </div>
                </div>
                <Input type="text" name="cardHolder" label="Card holder" placeholder=""
                    v-model:value="subscriptionData.cardHolder" />
                <Input type="email" name="email" label="Email" placeholder="" v-model:value="subscriptionData.email" />
                <Input type="text" name="address" label="Address" placeholder="" v-model:value="subscriptionData.address" />
                <Input type="text" name="phone" label="Phone number" placeholder=""
                    v-model:value="subscriptionData.phone" />


                <div class="editSubscription__inputWrapper">
                    <Button type="submit" class="editProfile__inputButton" :isProcessing="isProcessing">Go Premium</Button>
                </div>
            </Form>
        </div>
    </div>
    <div class="section section-editSubscription">
        <div class="subscription__history">
            <div class="subscriptionHistory__content">
                <div class="subscriptionHistory__header" @click="toggleTable">
                    <h3 class="subscriptionHistory__heading">Purchase history</h3>
                    <ChevronDownIcon />
                </div>
                <div v-show="isVisible">
                    <table class="table" v-if="hasHistory">
                        <thead>
                            <tr>
                                <th>Transaction ID</th>
                                <th>Start Date</th>
                                <th>End Date</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            <template v-for="(transaction, index) in history" :key="index">
                                <tr>
                                    <td>{{ transaction.transactionId }}</td>
                                    <td>{{ subscriptionDate(transaction.startDate) }}</td>
                                    <td>{{ subscriptionDate(transaction.endDate) }}</td>
                                    <td :class="{ 'subscription__statusActive': transaction.status === 'active' }">
                                        {{ subscriptionStatus(transaction.status) }}
                                    </td>
                                </tr>
                            </template>
                        </tbody>
                    </table>
                    <div class="subscriptionHistory__noHistory" v-else>No subscription history.</div>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
.subscriptionHistory__header {
    display: flex;
    justify-content: space-between;
    align-items: center;

    transition: .5s all;

    &:hover {
        cursor: pointer;
    }
}
</style>
