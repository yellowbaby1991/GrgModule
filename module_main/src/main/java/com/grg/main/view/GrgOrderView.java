package com.grg.main.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.grg.main.R;
import com.grg.main.databinding.OrderViewLayoutBinding;
import com.lib.common.utils.ToastUtils;
import com.xuexiang.xui.utils.ViewUtils;
import com.xuexiang.xui.widget.button.ButtonView;

import java.util.List;

/**
 * 点餐金额选择自定义控件
 */
public class GrgOrderView extends RelativeLayout implements View.OnClickListener {

    private int mTotolMoney = 0;

    //金额列表
    private List<String> mItems;

    //每行最大数量
    private int mRowMaxNum = 3;

    private Context mContext;

    private OrderViewLayoutBinding mBinding;

    public GrgOrderView(Context context) {
        this(context, null);
    }

    public GrgOrderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.order_view_layout, this, true);

        initEvents();
    }

    private void initEvents() {
        mBinding.cancelBt.setOnClickListener(this);
        mBinding.submitBt.setOnClickListener(this);
    }

    public void setItems(List<String> items) {
        mItems = items;
        createItemViews();
    }

    private void createItemViews() {

        int index = 0;
        int rowsNum = getRowsNum();

        for (int i = 0; i < rowsNum; i++) {
            LinearLayout rowsContainLl = new LinearLayout(mContext);
            rowsContainLl.setOrientation(LinearLayout.HORIZONTAL);
            for (int j = 0; j < mRowMaxNum; j++) {
                if (index >= mItems.size()) {
                    break;
                }
                RelativeLayout itemView = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.button_view_layout, this, false);//外围边框生效
                final ButtonView buttonView = itemView.findViewById(R.id.item_button_view);
                buttonView.setText(mItems.get(index++));
                buttonView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addChoiceItems(buttonView.getText().toString());
                    }
                });
                ViewUtils.fadeIn(itemView,1000,null);
                rowsContainLl.addView(itemView);
            }
            mBinding.itemViewsLl.addView(rowsContainLl);
        }


    }

    private void addChoiceItems(String item) {
        mTotolMoney += Integer.valueOf(item.replace(" 元", ""));
        RelativeLayout itemView = createChoiceItemView(item);
        ViewUtils.slideIn(itemView,500,null, ViewUtils.Direction.TOP_TO_BOTTOM);
        mBinding.choiceItemsLl.addView(itemView, 0);
        mBinding.totalMoneyStv.setCenterString("合计 " + mTotolMoney + " 元");
        ViewUtils.fadeIn( mBinding.totalMoneyStv,1000,null);

    }

    private RelativeLayout createChoiceItemView(String item) {
        RelativeLayout itemView = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.button_view_layout, this, false);//外围边框生效
        final ButtonView buttonView = itemView.findViewById(R.id.item_button_view);
        RelativeLayout.LayoutParams layoutParams = (LayoutParams) buttonView.getLayoutParams();
        layoutParams.width = 150;
        layoutParams.height = 50;
        layoutParams.leftMargin = 20;
        buttonView.setLayoutParams(layoutParams);
        buttonView.setText(item);
        return itemView;
    }

    private int getRowsNum() {
        int total = mItems.size();
        int rowsNum = total / mRowMaxNum;
        if (total % mRowMaxNum != 0) {
            rowsNum++;
        }
        return rowsNum;
    }

    private void cancelOrder(){
        mTotolMoney = 0;
        mBinding.choiceItemsLl.removeAllViews();
        mBinding.totalMoneyStv.setCenterString("合计 " + mTotolMoney + " 元");
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.cancel_bt) {
            cancelOrder();
        }
        if (id == R.id.submit_bt) {
            ToastUtils.show(mBinding.totalMoneyStv.getCenterString());
        }
    }
}
