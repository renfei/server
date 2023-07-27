# 使用 WebAPI 进行 AES-256-GCM 加解密

使用 WebAPI 进行 AES-256-GCM 加解密适配本程序后端。

交换逻辑请查看：[https://www.renfei.net/posts/1003346](https://www.renfei.net/posts/1003346)

```javascript
let data = b642ab("S3p/uDmrcG+uIFKf5f5X7oh/BVkS5l9F7NkzviLZijg9YO/vAhQtwwuyLhvm+dZ5FQ==");
let key= b642ab('ysjjEJjSfIBAP7FRH3WOcTrcU39j4tQ1YyipA9bev2E=');
let iv = b642ab('e7QLFuox9ejsmZnD');

// 导入秘钥
crypto.subtle.importKey(
    "raw",
    key,
    { name: "AES-GCM" },
    true,
    ["decrypt", "encrypt"]
).then(key=>{
    //解密
    window.crypto.subtle.decrypt({ name: "AES-GCM", iv }, key, data).then(res=>{
        console.log(new TextDecoder().decode(res))
        alert(new TextDecoder().decode(res));
    });
    // 加密
    let myData = new TextEncoder().encode("这是前端的消息");
    window.crypto.subtle.encrypt({ name: "AES-GCM", iv }, key, myData).then(res=>{
        console.log(res)
        console.log(window.btoa(String.fromCharCode(...new Uint8Array(res))))
        alert(window.btoa(String.fromCharCode(...new Uint8Array(res))))
    });
});

function b642ab(base64) {
    var binary_string = window.atob(base64);
    var len = binary_string.length;
    var bytes = new Uint8Array(len);
    for (var i = 0; i < len; i++) {
        bytes[i] = binary_string.charCodeAt(i);
    }
    return bytes.buffer;
}
```
