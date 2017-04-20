package com.mview.medittext.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.mview.medittext.R;
import com.mview.medittext.bean.common.CommonSelectData;
import com.mview.medittext.utils.CommonSelcetUtils;
import com.mview.medittext.view.toast.T;

import java.util.ArrayList;
import java.util.List;




/**
 * 工作类别视图
 * 
 * @author 王定波
 * 
 */
public class TicketWorkTypeGroupView extends GroupViewBase {

	private KingTellerEditText workType;
	private KingTellerEditText costMinute;
	private KingTellerEditText handleSub;
	private KingTellerEditText remark;
	private KingTellerEditText troubleReasonCode;
	private OnChangeListener onChangeListener;
	private Button btn_add, btn_delete;
	private boolean listflag;
	private List<String> workTypes;
	//	private boolean wlzcflag = false;
	
	private List<CommonSelectData> workTypeCommonSelectList;
	
	public TicketWorkTypeGroupView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public TicketWorkTypeGroupView(Context context, boolean isdel) {
		super(context, isdel);
	}
	
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		LayoutInflater.from(getContext()).inflate(R.layout.item_select_worktype,
				this);
		LayoutParams lp = new LayoutParams(
				LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		lp.setMargins(0, 0, 0, 25);
		setLayoutParams(lp);

		btn_add = (Button) findViewById(R.id.btn_add);
		btn_delete = (Button) findViewById(R.id.btn_delete);

		listflag = false;
		
		if (isdel){
			btn_delete.setVisibility(View.VISIBLE);
		}else{
			btn_delete.setVisibility(View.GONE);
		}

		btn_delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((LinearLayout) getParent()).removeView(TicketWorkTypeGroupView.this);
				if (onChangeListener != null) {
					onChangeListener.onWorkTypeChange(TicketWorkTypeGroupView.this, null);
				}
			}
		});

		btn_add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((LinearLayout) getParent()).addView(TicketWorkTypeGroupView.this.clone());
			}
		});
		btn_add.setVisibility(GONE);

		workType = (KingTellerEditText) findViewById(R.id.workType);
		workType.setLists(CommonSelcetUtils.getSelectList(CommonSelcetUtils.workType2));
		workTypeCommonSelectList = CommonSelcetUtils.getSelectList(CommonSelcetUtils.workType2);
		workType.setOnChangeListener(new KingTellerEditText.OnChangeListener() {
			@Override
			public void onChanged(CommonSelectData data) {

//			{ "故障维护：NORMAL", "软件升级：SOFT_UPGRAD", "调试机器：DEBUG", "PM：PM", "开通机器：START",
//			  "账务处理：ACCOUNTANT", "培训：TRAINING","接机：RECEIVE_MACHINE", "移机：MOVE_MACHINE",
//			  "退机：BACK_MACHINE", "安装机器：SETUP_MACHINE", "检查机器：CHECK_MACHINE", "其他：OTHER",
//			  "部件加装：ADD_COMPONENT","客服报废：SCRAP_MACHINE", "硬件改造：MODIFY_COMPONENT","停机：STOP_MACHINE"};
				try {
					List<String> list = ((GroupListView) ((LinearLayout) ((LinearLayout) getParent()).getParent()).getParent()).getListData();
					List<String> listtype = new ArrayList<String>();
					for (int j = 0; j < list.size(); j++) {
						listtype.add(getValueFromText(list.get(j), workTypeCommonSelectList));
					}
					
					//工作类别中包含故障维护时  工作类别不能选退机，客服报废，移机，开通机器共存
					if (listtype.contains("NORMAL")) {
						
						if (listtype.contains("BACK_MACHINE")||listtype.contains("SCRAP_MACHINE")||listtype.contains("MOVE_MACHINE")||listtype.contains("START")) {
							T.showShort(getContext(), "不能再选择："+data.getText()+"!");
							workType.setFieldTextAndValue("");
							listflag = false;
							return;
						}
					}
					//工作类别中包含PM时 工作类别不能选择退机，工程项目，开通机器
					if (listtype.contains("PM")) {
						if (listtype.contains("BACK_MACHINE")||listtype.contains("ENGINEERING_PROJECT")||listtype.contains("START")||listtype.contains("DEBUG")) {
							T.showShort(getContext(),"不能再选择："+data.getText()+"!");
							workType.setFieldTextAndValue("");
							listflag = false;
							return;
						}
					}
					//工作类别中包含开通机器时 工作类别不能选择移机，退机，客服报废
					if (listtype.contains("START")) {
						if (listtype.contains("MOVE_MACHINE")||listtype.contains("BACK_MACHINE")||listtype.contains("SCRAP_MACHINE")) {
							T.showShort(getContext(), "不能再选择："+data.getText() + "!");
							workType.setFieldTextAndValue("");
							listflag = false;
							return;
						}
					}
					//工作类别中包含调试机器 工作类别不能选择退机，客服报废
					if (listtype.contains("DEBUG")) {
						if (listtype.contains("BACK_MACHINE")||listtype.contains("SCRAP_MACHINE")) {
							T.showShort(getContext(), "不能再选择："+ data.getText() + "!");
							workType.setFieldTextAndValue("");
							listflag = false;
							return;
						}
					}
					//如果工作类别为 退机或者客服报废  工作类别不能选择其他任何一项
					if (listtype.contains("BACK_MACHINE")) {
						if (listtype.contains("NORMAL")||listtype.contains("PM")||listtype.contains("START")
							||listtype.contains("DEBUG")||listtype.contains("MOVE_MACHINE")
							||listtype.contains("CUSTOMER_ASSISTANCE")||listtype.contains("ENGINEERING_PROJECT")
							||listtype.contains("INSTALL_UPGRAD")||listtype.contains("SCARAP_MACHINE")) {
							T.showShort(getContext(), "不能再选择："+data.getText()+"!");
							workType.setFieldTextAndValue("");
							listflag = false;
							return;
						}
						
					}
					//如果工作类别为客服报废 工作类别不能选择其他任何一项
					if (listtype.contains("SCRAP_MACHINE")) {
						if (listtype.contains("NORMAL")||listtype.contains("PM")||listtype.contains("START")
							||listtype.contains("DEBUG")||listtype.contains("MOVE_MACHINE")
							||listtype.contains("CUSTOMER_ASSISTANCE")||listtype.contains("ENGINEERING_PROJECT")
							||listtype.contains("INSTALL_UPGRAD")||listtype.contains("BACK_MACHINE")) {
							T.showShort(getContext(), "不能再选择："+data.getText()+"!");
							workType.setFieldTextAndValue("");
							listflag = false;
							return;
						}
						
					}
				/*	//工作类别中包含 开通机器、调试机器、安装机器、接机中的任何一个时  工作类别都 不能再选择  退机  或  客服报废；
					if(listtype.contains("START") || listtype.contains("DEBUG") || listtype.contains("SETUP_MACHINE")
							|| listtype.contains("RECEIVE_MACHINE")){
						if(listtype.contains("BACK_MACHINE") || listtype.contains("SCRAP_MACHINE")){
							T.showShort(getContext(), "不能再选择：" + data.getText() + "!");
							workType.setFieldTextAndValue("");
							listflag=false;
							return;
						}
					}
					
					//工作类别中包含	退机  时工作类别  都不能再选择	开通机器,调试机器,安装机器,接机,客服报废   中的任何一个；    
					if(listtype.contains("BACK_MACHINE")){
						if(listtype.contains("START") || listtype.contains("DEBUG") || listtype.contains("SETUP_MACHINE") 
							|| listtype.contains("RECEIVE_MACHINE") || listtype.contains("SCRAP_MACHINE")){
							T.showShort(getContext(), "不能再选择：" + data.getText() + "!");
							workType.setFieldTextAndValue("");
							listflag=false;
							return;
						}
					}
					
					//工作类别中包含	客服报废   时工作类别  都不能再选择	开通机器,调试机器,安装机器,接机,硬件改造,退机,停机	中的任何一个
					if(listtype.contains("SCRAP_MACHINE")){
						if(listtype.contains("START") || listtype.contains("DEBUG") || listtype.contains("SETUP_MACHINE") 
							|| listtype.contains("RECEIVE_MACHINE") || listtype.contains("MOVE_MACHINE")
							|| listtype.contains("BACK_MACHINE") || listtype.contains("STOP_MACHINE")){
							T.showShort(getContext(), "不能再选择：" + data.getText() + "!");
							workType.setFieldTextAndValue("");
							listflag=false;
							return;
						}
					}
					*/
					//去重
					for (int j = 0; j < listtype.size(); j++) {
						for(int i = j;i <listtype.size();i++){
							if(i != j && listtype.get(i).equals(listtype.get(j))){
								T.showShort(getContext(), "不能重复选择同一个类别：" + data.getText() + "!");
								workType.setFieldTextAndValue("");
								listflag=false;
								return;
							}
						}
					}
					
				} catch (Exception e) {
									
				}
				
//				//判断打开报告状态？还是填写的状态
//				if(!wlzcflag){
//					listflag = false;
//					wlzcflag = true;
//				}
				
				listflag = true;
				
				if (onChangeListener != null && listflag) {
					onChangeListener.onWorkTypeChange(TicketWorkTypeGroupView.this, data);
				}
				
			//	listflag = true;
			}
		});
	}
	@Override
	public boolean checkData() {
		return false;
	}

	@Override
	public String getData() {
		return workType.getFieldText();
	}


	public void setOnChangeListener(OnChangeListener onChangeListener) {
		this.onChangeListener = onChangeListener;
	}
	

	public interface OnChangeListener {
		public void onWorkTypeChange(TicketWorkTypeGroupView view, CommonSelectData data);
	}

	@Override
	protected TicketWorkTypeGroupView clone() {
		TicketWorkTypeGroupView ca = new TicketWorkTypeGroupView(getContext(), true);
		ca.setOnChangeListener(onChangeListener);
		ca.setListflag(true);
		return ca;
	}
	
	public void setListflag(Boolean mListflag) {
		this.listflag = mListflag;
	}
	
	private String getValueFromText(String text,List<CommonSelectData> workTypeCommonSelectList){
		if (text != null && workTypeCommonSelectList != null) {
			for (int i = 0; i < workTypeCommonSelectList.size(); i++) {
				if (workTypeCommonSelectList.get(i).getText().equals(text)) {
					return workTypeCommonSelectList.get(i).getValue();
				}
			}
		}
		
		return null;
	}
}
