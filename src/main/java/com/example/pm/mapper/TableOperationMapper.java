package com.example.pm.mapper;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.pm.entity.AccountInfoEntity;
import com.example.pm.entity.UserInfoEntity;

@Mapper
public interface TableOperationMapper {
	//	ユーザー登録
	int userInfoInsert(UserInfoEntity uiEntity);
	//	userinfoのidを取得 引数はログイン時に用いたuserIdを使用
	int idOneGet(String userId);
	//	ログイン機能 カラムごとにデータを得る
	List<String> userIdAllGet();
	List<String> passWdAllGet();
	List<String> passKeyAllGet();
	//	ログインアカウント変更（パスワード更新）
	int passWdUpdate(String userId, String passWd);
	//	ログインアカウント変更（パスキー更新）
	void passKeyUpdate(String userId, String passKey);
	//	最終ログイン日を取得
	LocalDateTime lastLoginDateSelect(String userId);
	//	最終ログイン日を更新
	void lastLoginDateUpdate(String userId, LocalDateTime lastLoginDate);
	//	一つのパスワードを取得
	String passWdOneGet(String userId);	
	//	一つのパスキーを取得
	String passKeyOneGet(String userId);
	//	accountdataテーブルのid_userカラムで必要なuserinfoテーブルのidをuserIdをキーにして取得する
	Integer userInfoIdOneGet(String userId);
	//	アカウント登録
	void accountDataRegisterInsert(AccountInfoEntity aiEntity);
	//	accountdataテーブルから全件データを取得してリストに格納する
	List<AccountInfoEntity> findAllAccountData(int id, int id_user);
	/*
	 * 以下はregisterDataFromDBメソッドで使用するDAO
	 * userinfoテーブルのカラム「userId」とaccountdataテーブルのカラム「id_user」が合致するデータのみ取得する
	 */
	List<AccountInfoEntity> selectAllData(int id);
	/*
	 * アカウントデータを一行だけ論理削除
	 * @param dataDelete 実体はid
	 */
	int deleteARowOfData(Integer dataDelete);
	/*
	 * アカウントデータを一行削除後、auto_incrementを-1するためのメソッド
	 * ManagerController.javaで使用
	 */
	//	public void autoIncrementSubtractByMinus1(String dataDelete);
	
	//	アカウントデータ更新
	int updateAccountDataOneARow(Integer id, String userId, String passWd, String siteName);
	AccountInfoEntity selectOneData(int id);
	
	/*
	 * ログイン機能
	 * 一致したユーザーIDが存在しているか検証
	 * 一致したパスワードが存在しているか検証
	 * 一致したパスキーが存在しているか検証
	 */
	String userIdOneVerify(String userId);
	String passWdOneVerify(String passWd);
	String passKeyOneVerify(String passKey);
}
