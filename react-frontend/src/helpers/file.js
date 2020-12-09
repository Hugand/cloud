export function getFileSize(bytes,decimalPoint) {
    if(bytes === 0) return '0B';
    var k = 1000,
        dm = decimalPoint || 2,
        sizes = ['B', 'KB', 'MB', 'GB'],
        i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + sizes[i];
 }

 export function formatDate(date) { return (new Date(date)).toDateString().split(" ").slice(1).join(" ")}
