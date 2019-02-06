package com.github.codesniper.poplayer.pop;
//Thanks For Your Reviewing My Code 
//Please send your issues email to 15168264355@163.com when you find there are some bugs in My class 
//You Can add My wx 17620752830 and we can communicate each other about IT industry
//Code Programming By MrCodeSniper on 2018/10/27.Best Wishes to You!  []~(~▽~)~* Cheers!


import com.github.codesniper.poplayer.PopLayerView;
import com.github.codesniper.poplayer.strategy.LayerLifecycle;

import static com.github.codesniper.poplayer.config.LayerConfig.COUNTDOWN_CANCEL;
import static com.github.codesniper.poplayer.config.LayerConfig.TRIGGER_CANCEL;

/**
 * 多种多个弹窗 在同一时刻出现 需要纳入管理 进行显示先后顺序时间次数的封装 应对产品随时增加的需求
 * 以每个弹窗实体为单位 配置为可装配各种条件的弹窗
 * <p>
 * 窗口的最小不可分割实体 根据优先级进行排序
 */
public class Popi implements Comparable<Popi> {



    //弹窗类型
    private PopType mType;

    //弹窗的唯一标识  ID不同的弹窗 视为不同的弹窗 throught the view shows the same
    private int popId;

    //默认不进行 弹窗最大显示次数记录
    private int maxShowCount;

    //弹窗显示方式 默认为点击取消（需要设置显示次数 默认为Interger.Max-1） 计时取消(需要设置显示时间 默认显示时间60s )
    private int cancelType ;

    //默认弹窗显示时间 单位S
    private int maxShowTimeLength;

    //弹窗内容视图 可为空
    private LayerLifecycle content;

    //时间撮形式的开始时间和结束时间 1974-11-28 23:10:31~2460-10-21 13:32:10
    private long beginDate;
    private long endDate;

    //优先级 数值越小 优先级越高 这里考虑到弹窗的种类 不规定具体的优先级的等级 默认优先级最高
    private int priority;

    //对应弹窗的点击后路径 默认适用于单业务弹窗
    private String routePath;

    public Popi(Builder builder) {
        this.priority = builder.mPriority;
        this.popId = builder.mPopId;
        this.content = builder.mPopLayerView;
        this.mType = builder.mPopType;
        this.cancelType = builder.mCancelType;
        this.routePath = builder.mRoutePath;
        this.beginDate = builder.mBeginDate;
        this.endDate = builder.mEndDate;
        this.maxShowCount = builder.maxShowCount;//最大显示次数 在两种取消中默认使用
        //取消类型为倒计时取消
        if (cancelType == COUNTDOWN_CANCEL) {
            this.maxShowTimeLength = builder.maxShowTimeLength;
        } else if (cancelType == TRIGGER_CANCEL) { //取消类型为触发取消

        }
    }

    public int getPopId() {
        return popId;
    }

    public int getCancelType() {
        return cancelType;
    }

    public void setCancelType(int cancelType) {
        this.cancelType = cancelType;
    }


    public int getMaxShowTimeLength() {
        return maxShowTimeLength;
    }

    public void setMaxShowTimeLength(int maxShowTimeLength) {
        this.maxShowTimeLength = maxShowTimeLength;
    }

    public int getMaxShowCount() {
        return maxShowCount;
    }

    public void setMaxShowCount(int maxShowCount) {
        this.maxShowCount = maxShowCount;
    }

    public LayerLifecycle getContent() {
        return content;
    }

    public long getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(long beginDate) {
        this.beginDate = beginDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public int getPriority() {
        return priority;
    }


    public static class Builder {

        private LayerLifecycle mPopLayerView;
        private PopType mPopType;
        private int maxShowCount=Integer.MAX_VALUE - 1;
        private int mPopId;
        private long mBeginDate=154883431L;
        private long mEndDate=15488343130L;
        private int mPriority;
        private String mRoutePath;
        private int mCancelType=TRIGGER_CANCEL;
        private int maxShowTimeLength=60;

        public Builder setMaxShowTimeLength(int maxShowTimeLength) {
            this.maxShowTimeLength = maxShowTimeLength;
            return this;
        }

        public Builder setConcreateLayer(LayerLifecycle mPopLayerView) {
            this.mPopLayerView = mPopLayerView;
            return this;
        }

        public Builder setmPopType(PopType mPopType) {
            this.mPopType = mPopType;
            return this;
        }

        public Builder setMaxShowCount(int maxShowCount) {
            this.maxShowCount = maxShowCount;
            return this;
        }

        public Builder setmPopId(int mPopId) {
            this.mPopId = mPopId;
            return this;
        }

        public Builder setmBeginDate(long mBeginDate) {
            this.mBeginDate = mBeginDate;
            return this;
        }

        public Builder setmEndDate(long mEndDate) {
            this.mEndDate = mEndDate;
            return this;
        }

        public Builder setmPriority(int mPriority) {
            this.mPriority = mPriority;
            return this;
        }

        public Builder setmRoutePath(String mRoutePath) {
            this.mRoutePath = mRoutePath;
            return this;
        }

        public Builder setmCancelType(int mCancelType) {
            this.mCancelType = mCancelType;
            return this;
        }

        public Popi build() {
            return new Popi(this);
        }

        public void pushToQueue() {
            PopManager.getInstance(mPopLayerView.getLayerContext()).pushToQueue(build());
        }

    }

    /**
     * 自然排序即从小到大
     * 返回1的，代表此对象比参数对象大，排在后面，这样就可以控制降序或升序排列
     */
    @Override
    public int compareTo(Popi o) {
        if (this.priority > o.getPriority()) {
            return 1;
        } else if (this.priority < o.getPriority()) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return "Popi{" +
                "mType=" + mType +
                ", popId=" + popId +
                ", maxShowCount=" + maxShowCount +
                ", cancelType=" + cancelType +
                ", maxShowTimeLength=" + maxShowTimeLength +
                ", content=" + content +
                ", beginDate=" + beginDate +
                ", endDate=" + endDate +
                ", priority=" + priority +
                ", routePath='" + routePath + '\'' +
                '}';
    }
}
