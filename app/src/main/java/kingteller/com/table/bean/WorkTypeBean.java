package kingteller.com.table.bean;

import java.io.Serializable;

/**
 * 工作报告的工作类别表
 * 
 * @author 王定波
 * 
 */
public class WorkTypeBean implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id; // 主键id
	private String idLike;
	private String reportId; // 报告id
	private String reportIdLike;
	private String orderId; // 工单id
	private String orderIdLike;
	private String workType; // 工作类别
	private String workTypeLike;
	private Long costMinute; // 耗时分钟
	private Double costHour; // 耗时小时
	private String handleSub; // 处理过程
	private String handleSubLike;
	private String costMinuteStr;      // 耗时分钟
	private String costHourStr;      // 耗时小时
	private String reason;        // 本次故障原因
	private String reasonLike;//本次故障原因(0:人为,1:操作,2:常规,3:其他)
	private String handleSubId;     // 处理过程ID
	private String handleSubIdLike;
	private String remark;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIdLike() {
		return idLike;
	}
	public void setIdLike(String idLike) {
		this.idLike = idLike;
	}
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public String getReportIdLike() {
		return reportIdLike;
	}
	public void setReportIdLike(String reportIdLike) {
		this.reportIdLike = reportIdLike;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOrderIdLike() {
		return orderIdLike;
	}
	public void setOrderIdLike(String orderIdLike) {
		this.orderIdLike = orderIdLike;
	}
	public String getWorkType() {
		return workType;
	}
	public void setWorkType(String workType) {
		this.workType = workType;
	}
	public String getWorkTypeLike() {
		return workTypeLike;
	}
	public void setWorkTypeLike(String workTypeLike) {
		this.workTypeLike = workTypeLike;
	}
	public Long getCostMinute() {
		return costMinute;
	}
	public void setCostMinute(Long costMinute) {
		this.costMinute = costMinute;
	}
	public Double getCostHour() {
		return costHour;
	}
	public void setCostHour(Double costHour) {
		this.costHour = costHour;
	}
	public String getHandleSub() {
		return handleSub;
	}
	public void setHandleSub(String handleSub) {
		this.handleSub = handleSub;
	}
	public String getHandleSubLike() {
		return handleSubLike;
	}
	public void setHandleSubLike(String handleSubLike) {
		this.handleSubLike = handleSubLike;
	}
	public String getCostMinuteStr() {
		return costMinuteStr;
	}
	public void setCostMinuteStr(String costMinuteStr) {
		this.costMinuteStr = costMinuteStr;
	}
	public String getCostHourStr() {
		return costHourStr;
	}
	public void setCostHourStr(String costHourStr) {
		this.costHourStr = costHourStr;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getReasonLike() {
		return reasonLike;
	}
	public void setReasonLike(String reasonLike) {
		this.reasonLike = reasonLike;
	}
	public String getHandleSubId() {
		return handleSubId;
	}
	public void setHandleSubId(String handleSubId) {
		this.handleSubId = handleSubId;
	}
	public String getHandleSubIdLike() {
		return handleSubIdLike;
	}
	public void setHandleSubIdLike(String handleSubIdLike) {
		this.handleSubIdLike = handleSubIdLike;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	
	
}
