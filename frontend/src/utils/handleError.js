import Swal from 'sweetalert2';


export default (error, errorStore) => {
    console.log(error)

    const responseText = {
        '401': error?.response?.data,
        '403': error?.response?.data?.description,
        '404': error?.response?.data?.description,
        '422': error?.response?.data?.description,
        '500': 'Internal Server Error. Please try again later.',
        'undefined': 'Unexpected error occured. Please try again later.'
    }

    if(error?.response?.data?.fieldErrors) {
        errorStore.setErrors(error?.response?.data?.fieldErrors)
    }

    Swal.fire({
        title: responseText[error?.response?.status],
        icon: "error"
    });

}