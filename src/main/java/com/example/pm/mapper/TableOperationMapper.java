package com.example.pm.mapper;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import com.example.pm.entity.AccountInfoEntity;
import com.example.pm.entity.UserInfoEntity;

@Mapper
public interface TableOperationMapper {
	//	ユーザー登録
	public int userInfoInsert(UserInfoEntity uiEntity);
	//	userinfoのidを取得 引数はログイン時に用いたuserIdを使用
	public int idOneGet(String userId);
	//	ログイン機能 カラムごとにデータを得る
	public List<String> userIdAllGet();
	public List<String> passWdAllGet();
	public List<String> passKeyAllGet();
	//	ログインアカウント変更（パスワード更新）
	public int passWdUpdate(String userId, String passWd);
	//	ログインアカウント変更（パスキー更新）
	public void passKeyUpdate(String userId, String passKey);
	//	最終ログイン日を更新
	public void lastLoginDateUpdate(String userId, LocalDate lastLoginDate);
	//	一つのパスワードを取得
	public String passWdOneGet(String userId);	
	//	一つのパスキーを取得
	public String passKeyOneGet(String userId);
	//	accountdataテーブルのid_userカラムで必要なuserinfoテーブルのidをuserIdをキーにして取得する
	public Integer userInfoIdOneGet(String userId);
	//	アカウント登録
	public void accountDataRegisterInsert(AccountInfoEntity aiEntity);
	//	accountdataテーブルから全件データを取得してリストに格納する
	public List<AccountInfoEntity> findAllAccountData(int id, int id_user);
	/*
	 * 以下はregisterDataFromDBメソッドで使用するDAO
	 * userinfoテーブルのカラム「userId」とaccountdataテーブルのカラム「id_user」が合致するデータのみ取得する
	 */
	public List<AccountInfoEntity> selectAllData(int id);
	/*
	 * アカウントデータを一行だけ論理削除
	 * @param dataDelete 実体はid
	 */
	public int deleteARowOfData(Integer dataDelete);
	/*
	 * アカウントデータを一行削除後、auto_incrementを-1するためのメソッド
	 * ManagerController.javaで使用
	 */
	//	public void autoIncrementSubtractByMinus1(String dataDelete);
	
	//	アカウントデータ更新
	public int updateAccountDataOneARow(Integer id, String userId, String passWd, String siteName);
	AccountInfoEntity selectOneData(int id);
	
	/*
	 * ログイン機能
	 * 一致したユーザーIDが存在しているか検証
	 * 一致したパスワードが存在しているか検証
	 * 一致したパスキーが存在しているか検証
	 */
	public String userIdOneVerify(String userId);
	public String passWdOneVerify(String passWd);
	public String passKeyOneVerify(String passKey);
}
