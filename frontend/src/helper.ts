export function offsetDate(date: Date) {
    const timezoneOffset = date.getTimezoneOffset()
    date.setMinutes(date.getMinutes() - timezoneOffset);
    return date;
}