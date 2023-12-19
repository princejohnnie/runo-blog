<script setup>
import { ref, computed } from 'vue';
import { useUserStore } from '@/stores/user.js';
import ProfileHeader from "@/components/profile/ProfileHeader.vue";
import Input from '@/components/form/Input.vue';
import Form from '@/components/form/Form.vue';
import Button from '@/components/form/Button.vue';
import VueDatePicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css';
import ChevronDownIcon from '@/components/icons/ChevronDownIcon.vue'

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
    cardExpiryDate: '',
    name: '',
    surname: '',
    address: '',
    subscriptionType: 'monthly',
    email: '',

});

const isVisible = ref(false);

const history = [
    {
        transactionId: "1223",
        start: "10.10.2021",
        end: "10.10.22",
        status: "Active"
    },
    {
        transactionId: "564",
        start: "9.10.2021",
        end: "10.10.21",
        status: "Ended"
    }
]

const monthlyCost = ref(20);

const toggleTable = () => {
    isVisible.value = !isVisible.value;

};

const submitForm = async () => {
    //change to data
    const formData = new FormData();
    for (const key in subscriptionData.value) {
        formData.append(key, subscriptionData.value[key]);
    }

    console.log(formData);

    //const response = await userStore.editUser(userStore.user.id, formData);
    //await userStore.me();
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
    </div>
    <div class="section section-editSubscription">
        <div class="subscription__history">
            <div class="subscriptionHistory__content">
                <div class="subscriptionHistory__header" @click="toggleTable">
                    <h3 class="subscriptionHistory__heading">Purchase history</h3>
                    <ChevronDownIcon />
                </div>
                <table class="table" v-show="isVisible">
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
                                <td>{{ transaction.start }}</td>
                                <td>{{ transaction.end }}</td>
                                <td :class="{ 'subscription__statusActive': transaction.status === 'Active' }">
                                    {{ transaction.status }}
                                </td>
                            </tr>
                        </template>
                    </tbody>
                </table>
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
