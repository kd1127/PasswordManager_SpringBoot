package com.example.pm.dto;

import java.util.List;

import com.example.pm.entity.AccountInfoEntity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class AccountInfoDto {
	
	//	検索して取得した全データ
	private List<AccountInfoEntity> accountInfoList;
	
	//	ページング用リスト
	private List<AccountInfoEntity> pagingAccountInfoList;
	
	//	画面のコンボボックスで選択した表示件数
	private Integer displayCount;
	
	//	サイト名（検索ワード）
	private String siteName;
	
	//	ページ数（現在画面に表示しているページ数）
	private Long page;
	
	//	最大ページ数
	private Long maxPage;
	
	//	現在表示済みのデータ数
	private Integer showCompletionCount;
	
	//	次へボタン表示非表示フラグ
	private boolean nextButtonFlag;
	
	//	前へボタン表示非表示フラグ
	private boolean prevButtonFlag;
}
