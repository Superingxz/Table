package kingteller.com.table;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.mview.customdialog.view.dialog.NormalDialog;
import com.mview.customdialog.view.dialog.listener.OnBtnClickL;
import com.mview.customdialog.view.dialog.use.QPadPromptDialogUtils;
import com.mview.medittext.bean.common.CommonSelectData;
import com.mview.medittext.utils.KingTellerJudgeUtils;
import com.mview.medittext.view.GroupListView;
import com.mview.medittext.view.KingTellerEditText;
import com.mview.medittext.view.TicketWorkTypeGroupView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kingteller.com.table.adapter.TableAdapter;
import kingteller.com.table.bean.TableBean;
import kingteller.com.table.bean.WorkOrderBean;
import kingteller.com.table.printsdk.BluetoothUtil;
import kingteller.com.table.printsdk.DeviceListActivity;
import kingteller.com.table.utils.BitmapUtil;
import kingteller.com.table.view.toast.T;

import static kingteller.com.table.printsdk.PrintConfig.REQUEST_CONNECT_DEVICE;


public class SignTicketActivity extends BlueToothActivity implements View.OnClickListener
        , TicketWorkTypeGroupView.OnChangeListener{
    private TextView mTitle;
    private Button btnScanButton = null;
    private Button btnClose = null;
    private Button btn_BMP = null;
    private Button previewTicket;  //预览凭条

    private GroupListView workType_group_list;
    private KingTellerEditText et_khqm;
    private ImageView iv_khqm;
    private boolean mHasDraw;        //是否已经签字
    private Bitmap mSignBitmap;

    private LinearLayout table;
    private Bitmap mKffwBitmap;
    private ImageView khqm;
    private TextView clickToSign;
    private ListView content;
    private TableAdapter tableAdapter;
    private List<String> workTypes;
    private List<TableBean> tableBeanList;
    private List<CommonSelectData> commonSelectDatas;
    
    private WorkOrderBean mWorkOrderBean;
  
    private int addLine = 0;//过长的行数
    
    private int addNum = 0; //过长的项目个数
    
    private final static int  SIGNNAME = 100;
    
    private ImageView iv_ticket;
    
    public WorkOrderBean getRepairReportBean(){
        return mWorkOrderBean;
    }

    @Override
    protected int getLayout() {
        return R.layout.layout_sign_ticket;
    }

    @Override
    protected Context getContext() {
        return SignTicketActivity.this;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGNNAME && resultCode == RESULT_OK) {
            boolean hasDraw = data.getBooleanExtra("hasDraw",false);
            try {
                FileInputStream fis = new FileInputStream("/sdcard/SignName.png");
                mSignBitmap = BitmapFactory.decodeStream(fis);
                if (mSignBitmap != null && hasDraw) {
                    mHasDraw = hasDraw;
                    iv_khqm.setImageBitmap(mSignBitmap);
                    khqm.setVisibility(View.VISIBLE);
                    clickToSign.setVisibility(View.GONE);
                    khqm.setImageBitmap(mSignBitmap);
                    GetTicket();
                }
                fis.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
   
    @Override
    protected void initView() {
    	// Intent intent = getIntent();
    //	 mWorkOrderBean = (WorkOrderBean) intent.getSerializableExtra("WorkOrderBean");
        mTitle = (TextView) findViewById(R.id.title_right_text);
        	
        btnScanButton = (Button)findViewById(R.id.button_scan);
        btnScanButton.setOnClickListener(this);

        btnClose = (Button)findViewById(R.id.btn_close);
        btnClose.setOnClickListener(this);

        btn_BMP = (Button)findViewById(R.id.btn_prtbmp);
        btn_BMP.setOnClickListener(this);


        btnClose.setEnabled(false);
        btn_BMP.setEnabled(false);
        
        table = (LinearLayout) findViewById(R.id.table);
        khqm = (ImageView) findViewById(R.id.khqm);
        clickToSign = (TextView) findViewById(R.id.clicksign);
        content = (ListView) findViewById(R.id.content);
        workTypes = new ArrayList<>();
        tableBeanList = new ArrayList<>();
        commonSelectDatas = new ArrayList<>();
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");//可以方便地修改日期格式
        String time = dateFormat.format(date);
        TableBean tableBean1 = new TableBean("服务单号","WH/20161102/81834");
        tableBeanList.add(tableBean1);
        TableBean tableBean2 = new TableBean("服务日期",time);
        tableBeanList.add(tableBean2);
	    TableBean tableBean3 = new TableBean("客户简称","滨州邮政梁才支局");
	    tableBeanList.add(tableBean3);
        TableBean tableBean4 = new TableBean("ATM号","37005488");
        TableBean tableBean6 = new TableBean("服务工程师","张三、13414858915");
        tableBeanList.add(tableBean4);
        tableBeanList.add(tableBean6);
        tableAdapter = new TableAdapter(tableBeanList, getContext());
        content.setAdapter(tableAdapter);

        workType_group_list = (GroupListView) findViewById(R.id.workType_group_list);
        et_khqm = (KingTellerEditText) findViewById(R.id.et_khqm);
        iv_khqm = (ImageView) findViewById(R.id.iv_khqm);
        previewTicket = (Button) findViewById(R.id.previewTicket);

        /**-工作类别View-**/
        //工作类别-自定义添加删除视图view
        workType_group_list.setAddViewCallBack(new GroupListView.AddViewCallBack() {
            @Override
            public void addView(GroupListView view) {
                TicketWorkTypeGroupView wview = new TicketWorkTypeGroupView(SignTicketActivity.this, true);
                wview.setOnChangeListener(SignTicketActivity.this);
                wview.setListflag(true);
                view.addItem(wview);
            }
        });

        //工作类别视图
        TicketWorkTypeGroupView wview = new TicketWorkTypeGroupView(SignTicketActivity.this, false);//工作类别视图
        wview.setOnChangeListener(this);
        workType_group_list.addItem(wview);
        wview.setListflag(true);
        workType_group_list.findViewById(R.id.add_workType).setVisibility(View.VISIBLE);
    }

    @Override
    protected void MessageStateChange(int state) {
        switch (state) {
            case BluetoothUtil.STATE_CONNECTED:
                mTitle.setText(R.string.title_connected_to);
                mTitle.append(mConnectedDeviceName);
                btnScanButton.setEnabled(false);
                btnClose.setEnabled(true);
                btn_BMP.setEnabled(true);
                previewTicket.setEnabled(true);
                break;
            case BluetoothUtil.STATE_CONNECTING:
                mTitle.setText(R.string.title_connecting);
                break;
            case BluetoothUtil.STATE_LISTEN:
            case BluetoothUtil.STATE_NONE:
                mTitle.setText(R.string.title_not_connected);
                btnScanButton.setEnabled(true);
                btnClose.setEnabled(false);
                btn_BMP.setEnabled(false);
                previewTicket.setEnabled(false);
                break;
            case BluetoothUtil.BLUETOUCH_DISCONNECT:
                openBlueTooth();
                break;
            default:
                break;
        }
    }

    @Override
    protected void KeyListenerInit() {
        et_khqm.setOnDialogClickLister(new KingTellerEditText.OnDialogClickLister() {
            @Override
            public void OnDialogClick() {
            	//writeTabletDialog.show();
                Intent intent = new Intent(getContext(), SignPrintActivity.class);
                startActivityForResult(intent,SIGNNAME);
            }
        });
        iv_khqm.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//writeTabletDialog.show();
                Intent intent = new Intent(getContext(), SignPrintActivity.class);
                startActivityForResult(intent,SIGNNAME);
			}
		});
        previewTicket.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.previewTicket:
                PreviewTicket();
                break;
            case R.id.button_scan:{
                Intent serverIntent = new Intent(getContext(), DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                break;
            }
            case R.id.btn_close:{
                mService.stop();
                btnClose.setEnabled(false);
                btn_BMP.setEnabled(false);
                btnScanButton.setEnabled(true);
                btnScanButton.setText(getText(R.string.connect));
                break;
            }
            case R.id.btn_prtbmp:{
            	if (workTypes.size()==0) {
            		T.showShort(getContext(), "请先选择工作类型!");
            		return;
        		}
            	List<String> notSelectWorkType = new ArrayList<>();
            	for (int i = 0; i < workTypes.size(); i++) {
            		if (KingTellerJudgeUtils.isEmpty(workTypes.get(i))) {
        				notSelectWorkType.add(" "+ (i+1) +" ");
        			}
        		}
            	if (notSelectWorkType.size() > 0) {
            		StringBuffer sb = new StringBuffer();
            		for (int j = 0; j < notSelectWorkType.size(); j++) {
            			sb.append(notSelectWorkType.get(j));
        			}
            		final NormalDialog dialog_not_select_worktype = new NormalDialog(getContext());
    	        	QPadPromptDialogUtils.showOnePromptDialog(dialog_not_select_worktype, "工作类别"+sb.toString()+"没有选择！",
    						new OnBtnClickL() {
    							@Override
    							public void onBtnClick() {
    								dialog_not_select_worktype.dismiss();
    							}
    	                    });
            		T.showShort(getContext(), "工作类别"+sb.toString()+"没有选择！");
            		return;
        		}
            	if (mSignBitmap == null) {
            		T.showShort(getContext(), "没有签字，请先签字!");
            		return;
        		}
                if (mBitmap == null ) {
                    T.showShort(getContext(), "没有获取图片");
                } else {
                	  Print_BMP(mBitmap,true,true);
                }
                break;
            }
            default:
                break;
        }
    }
    
    /**
     * 预览凭条
     */
    private void PreviewTicket() {
    	List<String> notSelectWorkType = new ArrayList<>();
    	List<String> workTypes = workType_group_list.getListData();
    	for (int i = 0; i < workTypes.size(); i++) {
    		if (KingTellerJudgeUtils.isEmpty(workTypes.get(i))) {
				notSelectWorkType.add(" "+ (i+1) +" ");
			}
		}
    	if (notSelectWorkType.size() == workTypes.size()) {
        	T.showShort(getContext(), "请先选择工作类型!");
        	return;
		}
    	if (notSelectWorkType.size() > 0) {
    		StringBuffer sb = new StringBuffer();
    		for (int j = 0; j < notSelectWorkType.size(); j++) {
    			sb.append(notSelectWorkType.get(j));
			}
    		T.showShort(getContext(), "工作类别"+sb.toString()+"没有选择!");
    		return;
		}
    	if (mSignBitmap == null) {
    		T.showShort(getContext(), "请先签字!");
    		return;
		}
        if (mBitmap == null) {
            T.showShort(getContext(), "没有获取图片!");
        } else {
            BitmapUtil.saveBitmap2Path(mBitmap,"KingTellerReport");
            Intent intent = new Intent(getContext(), SignPrintPreviewActivity.class);
            startActivity(intent);
        }
    }

   public void GetTicket(){
	   table.post(new Runnable() {
           @Override
           public void run() {
               mBitmap = drawBitmapFromView(table);
               if (mBitmap == null) {
                   T.showShort(getContext(), "没有获取图片!");
               } else {
                   BitmapUtil.saveBitmap2Path(mBitmap,"KingTellerReport");
                  // iv_ticket.setImageBitmap(mBitmap);
               }
           }
       });
   }

    @Override
    public void onWorkTypeChange(TicketWorkTypeGroupView view, CommonSelectData data) {
    	addLine = 0;//重置
    	addNum = 0;
    	if (workTypes.size() > 0) {
    		for (int i = 0; i < workTypes.size()+1; i++) {
                tableBeanList.remove(tableBeanList.size() - (workTypes.size() +1) + i);
    		} 
    		workTypes.removeAll(workTypes);
		}
        workTypes.addAll(workType_group_list.<String>getListData());
        if (workTypes.size() > 0) {
            for (int i = 0; i < workTypes.size(); i++) {
                TableBean tableBean = new TableBean("服务类别" + (i + 1), workTypes.get(i));
                tableBeanList.add(tableBean);
            }
            TableBean tableBean5 = new TableBean("是否更换备件","是/欧姆龙读卡器"); 
            tableBeanList.add(tableBean5);
        }
        tableAdapter.notifyDataSetChanged();
        addLine += getMoreLineNum(tableBeanList);  //获取未显示的行数和未显示项的个数
        setListViewHeightBasedOnChildren(content); 
        GetTicket();
        
    }
    
    /**
     * 获取未显示的行数和未显示项的个数
     * @param tableBeanList
     * @return
     */
    private int getMoreLineNum(List<TableBean> tableBeanList) {
    	int num = 0;
    	if (tableBeanList.size() > 0) {
			for (int i = 0; i < tableBeanList.size(); i++) {
				String title  = tableBeanList.get(i).getTitle();
				String content= tableBeanList.get(i).getContent();
			    int title_line_num = length(title) / 12;
			    int content_line_num = length(content) / 22;
			    if (title_line_num < content_line_num && content_line_num > 0) {
			    	if (length(content) % 22 == 0) {
			    		num += content_line_num - 1;
					}else {
						num += content_line_num;
					}
			    	addNum += 1;
				}else if (title_line_num > content_line_num) {
					if (length(title) % 12 == 0) {
						num += title_line_num - 1;
					}else {
						num += title_line_num;
					}
					addNum += 1;
				}
			}
		}
		return num;
	}
    
    //重新测量
	public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        if (tableAdapter == null) {
            return;
        }

        int totalHeight = 0;
        int mItemHeight = 0; //每一行字的高度
        for (int i = 0; i < tableAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = tableAdapter.getView(i, null, listView);
//            int desiredWidth= View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
            listItem.measure(0, 0); // 计算子项View 的宽高
            int height = listItem.getMeasuredHeight();

            if (mItemHeight == 0 || height < mItemHeight) {
                mItemHeight = height - listView.getDividerHeight() * 2 * 3;
            }
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
       /* if (tableAdapter.getAddLine() > 0) {
			totalHeight += (mItemHeight + listView.getDividerHeight()) * tableAdapter.getAddLine() + listView.getDividerHeight() * tableAdapter.getAddNum() * 3;
		}*/
        if (addLine > 0) {
            totalHeight += (mItemHeight ) * addLine - listView.getDividerHeight() *  ( addNum * 3 + 1);
        }
        totalHeight += listView.getDividerHeight() * (tableAdapter.getCount() - 1) ;
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight ;
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }
    
    /**
     * 获取字符串的长度，如果有中文，则每个中文字符计为2位
     * @param value 指定的字符串
     * @return 字符串的长度
     */
    public  int length(String value) {
        int valueLength = 0;
      	if (value != null) {
      		String chinese = "[\u0391-\uFFE5]";
            /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
            for (int i = 0; i < value.length(); i++) {
                /* 获取一个字符 */
                String temp = value.substring(i, i + 1);
                /* 判断是否为中文字符 */
                if (temp.matches(chinese)) {
                    /* 中文字符长度为2 */
                    valueLength += 2;
                } else {
                    /* 其他字符长度为1 */
                    valueLength += 1;
                }
            }
    	}
        return valueLength;
    }
    
    /**
     * View转化为Bitmap
     * @param v
     * @return
     */
    private Bitmap drawBitmapFromView(View v) {
        Bitmap bitmap = null;
        v.measure(0, 0);
        int width = v.getMeasuredWidth();
        int height = v.getMeasuredHeight();
        if (width != 0 && height != 0) {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
            v.draw(canvas);
        }
        return bitmap;
    }
}
