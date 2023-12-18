import dayjs from 'dayjs';
import relativeTime from 'dayjs/plugin/relativeTime';

const formatDate = (date) => {
    return dayjs(date).format('DD-MM-YYYY');
}

const formatTime = (date) => {
    dayjs.extend(relativeTime);
    return dayjs(date).fromNow();
}

export default { formatDate, formatTime }