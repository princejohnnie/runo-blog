<script setup>
import { ref, computed } from 'vue';
import { useUserStore } from '@/stores/user.js';
import ProfileHeader from "@/components/profile/ProfileHeader.vue";
import Input from '@/components/form/Input.vue';
import Form from '@/components/form/Form.vue';
import Button from '@/components/form/Button.vue';
import VueDatePicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css'

const userStore = useUserStore();
const links = [
    {
        text: "Edit profile",
        to: { name: "edit-profile" }
    },
    {
        text: "Manage subscriptions",
        to: { name: "home" }
    },
]


const subscriptionData = ref({
    cardNumber: '',
    cardCcv: '',
    cardExpiryMonth: '',
    cardExpiryYear: '',
    name: '',
    surname: '',
    address: '',
    subscriptionType: '',

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

const toggleTable = () => {
    isVisible.value = !isVisible.value;

};

const submitForm = async () => {

    const formData = new FormData();
    for (const key in subscriptionData.value) {
        formData.append(key, subscriptionData.value[key]);
    }

    console.log(formData);

    //const response = await userStore.editUser(userStore.user.id, formData);
    //await userStore.me();
};


</script>
<template>
    <ProfileHeader :links="links" />

    <div class="section">
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
                        <highlight>20€</highlight> per month
                    </div>
                    <div class="editSubscription__cost">Total: <highlight>20€</highlight>
                    </div>
                </div>


                <Input type="text" name="number" label="Card number" placeholder=""
                    v-model:value="subscriptionData.cardNumber" />


                <!--*****************TO DO************************************-->
                <div class="editSubscription__gridOptions">
                    <Input type="text" name="cvv" label="CVV" placeholder="" v-model:value="subscriptionData.cardCcv"
                        style="width:100%" />

                    <div class="editSubscription__gridOption">
                        <label class="modal__inputLabel">Expiry date</label>
                        <VueDatePicker class="editSubscription__date" v-model="subscriptionData.cardExpiryMonth" monthPicker
                            required hideInputIcon placeholder="12/2023"></VueDatePicker>
                    </div>
                </div>


                <Input type="text" name="name" label="Name" placeholder="Your name" v-model:value="subscriptionData.name" />
                <Input type="text" name="surname" label="Surname" placeholder="Your surname"
                    v-model:value="subscriptionData.surname" />
                <Input type="text" name="address" label="Adress" placeholder="" v-model:value="subscriptionData.address" />

                <div class="editSubscription__inputWrapper">
                    <Button type="submit" class="editProfile__inputButton" :isProcessing="isProcessing">Go Premium</Button>
                </div>
            </Form>
        </div>
    </div>
    <div class="section">
        <div class="subscription__history">
            <div class="subscriptionHistory__content">
                <h3 class="subscriptionHistory__heading" @click="toggleTable">Purchase history</h3>
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
                                <td :class="{ 'subscription__active': transaction.status === 'Active' }">
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

<style scoped></style>
