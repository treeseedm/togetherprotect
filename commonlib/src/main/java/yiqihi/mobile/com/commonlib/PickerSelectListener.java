package yiqihi.mobile.com.commonlib;

public interface PickerSelectListener {
    public void onOKClick(String year,String month,String day);
    public void onCancleClick();
    public void onOKClick(int index,String value);
}
