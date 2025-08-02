package com.example.pm.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.pm.service.ApplicationService;
import com.example.pm.mapper.TableOperationMapper;
import com.example.pm.entity.AccountInfoEntity;

@Controller
@Transactional
public class ManagerController {
	@Autowired private ApplicationService service;
	@Autowired private TableOperationMapper mapper;
	@Autowired private LoginController loginController;
	
	private List<AccountInfoEntity> accountInfoEntityList = new ArrayList<>();;
	
	@PostMapping("/dataRegister")
	public String dataRegister(@ModelAttribute AccountInfoEntity aiEntity, Model model) {
		aiEntity.setInp_date(service.tableInsertpreparation());
		aiEntity.setId_user(mapper.userInfoIdOneGet(loginController.userInfoEntity.getUserId()));
		mapper.accountDataRegisterInsert(aiEntity);
		model.addAttribute("aiEntity", aiEntity);
		return "passwordManager/dataRegister";
	}
	
	@GetMapping("/registerDataShow")
	public String registerDataShow(Model model) {
		if(loginController.loginFlag) {
			//	ログイン時に入力したユーザーIDをコントローラークラスから取得する。
			int id = mapper.idOneGet(loginController.userInfoEntity.getUserId());
			//	userinfoテーブルのidをキーに、accountdataテーブルからid_userと一致するデータを取得		
			accountInfoEntityList = mapper.selectAllData(id);
			List<Integer> maxList = new ArrayList<>();
			
			//	画面側で取得したデータ数分だけ表示させるため、最大数までの数字をリストに格納
			for(int i=0; i<accountInfoEntityList.size(); i++) {
				maxList.add(i);
			}
			
			//	画面に表示させるため、アトリビュート
			model.addAttribute("maxList", maxList);
			model.addAttribute("accountInfoEntity", accountInfoEntityList);
			return "passwordManager/registerDataShow";
		}
		else {
			return "passwordManager/login";
		}
	}
	
	@PostMapping(value="/registerDataDel")
	public String registerDataDel(@RequestParam String dataDelete, Model model) {
		Integer id = Integer.parseInt(dataDelete);
		//	データ削除処理とauto_incrementを-1にする処理を追加
		int deleteRow =  mapper.deleteARowOfData(id);
		
		if(deleteRow < 1) {
			String infoMessage = "削除に失敗しました。";
			
			AccountInfoEntity aiEntity = new AccountInfoEntity();
			model.addAttribute("aiEntity", aiEntity);
			model.addAttribute("infoMessage", infoMessage);
			return "passwordManager/topPage"; 
		}
		return "passwordManager/registerDataDel"; 
	}
	
	@RequestMapping(value="/registerDataUpd", method=RequestMethod.GET)
	public String registerDataUpd(Model model, @RequestParam String index) {
		if(loginController.loginFlag) {
			try {
				//	indexfをキーにDBから取得してaccountInfoEntityに格納する
				AccountInfoEntity accountInfoEntity = mapper.selectOneData(Integer.parseInt(index));
				model.addAttribute("accountInfoEntity", accountInfoEntity);
			} catch (NumberFormatException e) {
				// TODO 自動生成された catch ブロック
				String errorMsg = "エラーが発生しました。数値には変換できない値が渡されました。";
				model.addAttribute("errorMsg", errorMsg);
				return "passwordManager/registerDataShow";
			}
			return "passwordManager/registerDataUpd";
		}
		else {
			return "passwordManager/login";
		}
	}
	
	@RequestMapping(value="/dataUpdComplete", method=RequestMethod.GET)
	public String dataUpdComplete(@ModelAttribute AccountInfoEntity aiEntity, Model model) {
		if(loginController.loginFlag) {
			try {
				int updateRows = mapper.updateAccountDataOneARow(aiEntity.getId(), aiEntity.getUserId(), aiEntity.getPassWd(), 
						aiEntity.getSiteName());
				
				if(updateRows < 1) {
					String errorMsg = "何らかの原因により、更新に失敗しました。";
					model.addAttribute("errorMsg", errorMsg);
					return "passwordManager/registerDataUpd";
				}
			} catch (DataAccessException ex) {
				String errorMsg = "データアクセスエラーが発生しました。";
				model.addAttribute("errorMsg", errorMsg);
				ex.printStackTrace();
				return "passwordManager/registerDataUpd";
			} catch (Exception e) {
				// TODO 自動生成された catch ブロック
				String errorMsg = "予期しないエラーが発生しました。";
				e.printStackTrace();
				model.addAttribute("errorMsg", errorMsg);
				return "passwordManager/registerDataUpd";
			}
			return "passwordManager/dataUpdComplete";
		}
		else {
			return "passwordManager/login";
		}
	}
}

//System.out.println("userId: " + uiEntity.getUserId());
//System.out.println("passWd: " + uiEntity.getPassWd());
//System.out.println("re_PassWd: " + uiEntity.getRe_PassWd());
//System.out.println("passKey: " + uiEntity.getPassKey());

//	デバッグ
//System.out.println("id: " + id);
//maxList.forEach(s -> System.out.println(s));
//userIdList.forEach(s ->System.out.println(s));