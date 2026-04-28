"use strict";

async function javatoJavasctipt(){
	console.log("クリックされた");
	if(!navigator.onLine){
		console.log('インターネット接続がありません。');
		throw new Error('インターネット接続がありません。');
	}
    return await new Promise((resolve, reject) => {
		fetch('api/pdfOutput', {
			method: "GET"
		})
	    	.then(response => {
				if(!response.ok){
					alert('HTTP通信失敗: ' + response.status);
					throw new Error('HTTP通信失敗: ' + response.status);
				}
				return response.json();
			})
			.then(data => {
				if(!data){
					throw new Error('レスポンスデータは空')
				}
				alert(JSON.stringify(data));
				resolve(data);
			})
			.catch(error => {
				console.log('HTTPエラー', error);
				reject(error);
			});
		});
}