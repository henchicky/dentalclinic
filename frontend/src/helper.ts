export function offsetDate(date: Date) {
    const timezoneOffset = date.getTimezoneOffset()
    date.setMinutes(date.getMinutes() - timezoneOffset);
    return date;
}

export function getTommorrow() {
    const tommorrow = new Date()
    tommorrow.setHours(0, 0, 0, 0)
    offsetDate(tommorrow)
    tommorrow.setDate(tommorrow.getDate() + 1)
    return tommorrow
}