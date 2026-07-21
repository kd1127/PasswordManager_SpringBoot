 "use strict";

//	ログインアカウント編集画面で使用
function confirmLogic(){
	//	書き換えURL
	let actionUrl = "http://localhost:8080/loginAccountEdit";
	//	入力フォームの値を取得			 
	 let passWd = document.getElementById('passWd');
	 let rePassWd = document.getElementById('re_PassWd');
	 
	 if(passWd.value != rePassWd.value){
		 alert('パスワードとパスワード（確認）が異なります。');
		 document.getElementById("form1").method = 'GET';
	 	 document.getElementById("form1").action = actionUrl;
	 	 return;
	 }
	 
	 if(confirm('登録してもよろしいですか？\n新パスワード：' + passWd.value)){ 
		 console.log(passWd.value);
	 }
	 else{
		//	confirmでキャンセルを押下
		document.getElementById("form1").method = 'GET';
	 	document.getElementById("form1").action = actionUrl;
	 }
}

//	パスキー編集画面で使用
async function confirmLogic2(){
	//	書き換えURL
	let actionUrl = "http://localhost:8080/passKeyEdit";
	//	入力フォームの値を取得			 
	 let passKey = document.getElementById('passKey');
	 
	if(passKey == null || passKey.value == ""){
		alert('パスキーを入力してください。');
		document.getElementById("form2").method = 'GET';
		document.getElementById("form2").action = actionUrl;
	}
	else if(passKey.value.length != 2){
		 alert('2文字のみを入力してください。');
		 document.getElementById("form2").method = 'GET';
		 document.getElementById("form2").action = actionUrl;
	}
	else{ 
		 if(confirm('登録してもよろしいですか？\n新パスワード：' + passKey.value)){ 
			 console.log(passKey.value);
		 }
		 else{
			//	confirmでキャンセルを押下
			document.getElementById("form2").method = 'GET';
		 	document.getElementById("form2").action = actionUrl;
		 }
	}
}

//	registerDataShow.htmlでデータ消去の際に、呼び出す関数
//	何故かconfirmのキャンセルボタンを押下しても自画面遷移されないから、一旦放置
function registerDataDelete(){
	let actionUrl = "http://localhost:8080/registerDataShow";
	
	 if(confirm('削除してもよろしいですか？')){
		 document.getElementById("form3").submit();
	 }
	 else{
		//	confirmでキャンセルを押下
		document.getElementById("form3").method = "GET";
		document.getElementById("form3").action = actionUrl;
		document.getElementById("form3").submit();
	 }
}

//	トップページで登録ボタンを押下した際にフロント側の処理をする関数
function dataRegister(){		
	//	POST通信に設定	
	document.getElementById('form').method = 'POST';
	let actionUrl = "http://localhost:8080/topPage";
	//	入力フォームの値を取得
	let create = document.getElementById('create');
	let userId = document.getElementById('userId');
	let siteName = document.getElementById('siteName');
	
	if(create.value == "" || userId == ""){
		alert('ユーザーID、パスワードを入力してください。');
		document.getElementById('form').method = "GET";
		document.getElementById('form').action = actionUrl;
	}
	else{
		 if(confirm('登録してもよろしいですか？\nPassword: ' + create.value + '\nuserId: ' + userId.value + '\nsiteName: ' + siteName.value)){ 
			 let actionUrl = 'http://localhost:8080/dataRegister';
			 document.getElementById('form').action = actionUrl;
		 }
		 else{
			//	confirmでキャンセルボタンを押下
		 	document.getElementById("form").action = actionUrl;
		 }
	}
}

//	乱数を得る
function getRandomInt(max){
	return Math.ceil(Math.random() * max);
}

//	トップページ画面で使用 チェックボックスの状態によって特定の文字列を表示させる
function Choice(){
	let situation = document.getElementById('situation');
	let situation2 = document.getElementById('situation2');
	let cryptography = document.getElementById('cryptography');
	
	//	ランダムパスワード方式
	if(situation.checked === true){
		//	cryptography.value="パスワード長を入力してください。";
		cryptography.placeholder="12文字以上24文字以下のパスワード長を入力してください。";
	}
	if(situation2.checked === true){	//	シーザー暗号
		//	cryptography.value="元となるパスワードを入力してください";
		cryptography.placeholder="元となるパスワードを入力してください";
	}
}

//	ひらがなが入力されたか検証する関数
function isHiragana(str){
	str = (str == null)?"":str;
	
	let check = false;
	if(str.match(/^[ぁ-ん]*$/)) {
		check = true;
	}
	else{
		check = false;
	}
	return check;
}

//	2種類の暗号方式によってパスワードを生成する
function Generate(){
	let cryptography = document.getElementById('cryptography');
	let create = document.getElementById('create');
	let create2 = document.getElementById('create2');
	
	if(cryptography.value === ""){
		alert("暗号方式を入力してください。");
	}
	//	ひらがなが入力された際のエラーハンドリング処理を作る
	let check = isHiragana(cryptography.value);
	if(check === true){
		alert("アルファベットを入力してください。");
	}
	
	const alphabet_list = new Array('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
				  'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 
				  'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
				  'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z');
	const number_list = new Array('0', '1', '2', '3', '4', '5', '6', '7', '8', '9');
	const symbol_list = new Array('.', '-',  '_',  ':', ';', '&',  '!', '?', '/');
	//	生成したパスワードを格納する変数
	let randomPassWd = new String();
	let caesarPassWd = new String();
	
	if(cryptography.value.length == 2){	//	ランダムパスワード方式
		for(let i=0; i<cryptography.value; i++){
			//	1=英字　2=数字　3=記号　どの文字を使うか乱数で決める
			let kinds = getRandomInt(3);
			
			if(kinds === 1){
				let alphabet = getRandomInt(52)-1;
				randomPassWd = randomPassWd.concat('', alphabet_list[alphabet]);
			}
			if(kinds === 2){
				let number = getRandomInt(10)-1;
				randomPassWd = randomPassWd.concat('', number_list[number]);
			}
			if(kinds === 3){
				let symbol = getRandomInt(9)-1;
				randomPassWd = randomPassWd.concat('', symbol_list[symbol]);
			}
		}
		create.value=randomPassWd;
		if(create2 != null){
			create2.value=randomPassWd;
		}
	}
	else{			//	シーザー暗号方式
		let base = Array.from(cryptography.value);
		for(let i=0; i<base.length; i++){
			let alphabet_flag = false;
			let number_flag = false;
			let symbol_flag = false;
			for(let j=0; j<alphabet_list.length; j++){
				if(base[i] === alphabet_list[j]){
					if(j <= 4){
						caesarPassWd = caesarPassWd.concat('', alphabet_list[j+47]);
					}
					else{
						caesarPassWd = caesarPassWd.concat('', alphabet_list[j-5]);
					}
					alphabet_flag = true;
					break;
				}
			}
			if(alphabet_flag === false){
				for(let j=0; j<number_list.length; j++){
					if(base[i] === number_list[j]){
						if(j <= 4){
							caesarPassWd = caesarPassWd.concat('', number_list[j+5]	);
						}
						else{
							caesarPassWd = caesarPassWd.concat('', number_list[j-5]	);
						}
						number_flag = true;
						break;
					}
				}
				if(number_flag === false){
					for(let j=0; j<symbol_list.length; j++){
						if(base[i] === symbol_list[j]){
							if(j <= 4){
								caesarPassWd = caesarPassWd.concat('', symbol_list[j+4]);
							}
							else{
								caesarPassWd = caesarPassWd.concat('', symbol_list[j-5]);
							}
							symbol_flag = true;
							break;
						}
					}
					if(symbol_flag === false){
						caesarPassWd = caesarPassWd.concat('', base[i]);
					}
				}
			}
		}
		create.value=caesarPassWd;
		if(create2 != null){
			create2.value=caesarPassWd;
		}
	}
}

function save(){
	let actionUrl = "http://localhost:8080/registerDataShow";
	let userId = document.getElementById('userId');
	let passWd = document.getElementById('passWd');
	let siteName = document.getElementById('siteName');
	
	if(userId.value == "" && passWd.value == "" && siteName.value == ""){
		alert("ユーザーID、パスワード、サイト名いずれかを入力してください。");
		return;
	}
	
	if(!confirm('以下のデータで更新してもよろしいですか？\n ユーザーID: ' + userId.value + '\n パスワード: ' + passWd.value + '\n サイト名: ' + siteName.value)){
		document.getElementById('form1').method = 'GET'
		document.getElementById('form1').action = actionUrl;
	}
}

function inputMailAdress(){
	const url = "http://localhost:8080/inputMailAddress";
	
	document.getElementById('form').method="GET"
	document.getElementById('form').action = url;
}

function PasswordChange(){
	const url = "http://localhost:8080/passwordReConfigure";
	const password = document.getElementById('password');
	const rePassword = document.getElementById('rePassword');
	const form = document.getElementById('form');
	
	if(password.value != rePassword.value){
		alert("パスワードとパスワード（確認）が一致していません。");
		form.method = "GET";
		form.action = url;
	}
}