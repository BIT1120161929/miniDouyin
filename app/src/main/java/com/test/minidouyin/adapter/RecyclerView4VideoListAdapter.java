package com.test.minidouyin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.test.minidouyin.R;
import com.test.minidouyin.network.beans.Feed;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerView4VideoListAdapter extends RecyclerView.Adapter {

    private static final int VIDEO_LIST = 0;
    private static final int USER_ICON = 1;

    private Context mContext;

    private List<Feed> feedList;
    public RecyclerView4VideoListAdapter(Context mContext, List<Feed> feedList) {
        this.mContext = mContext;
        this.feedList = feedList;

    }

    @Override
    public int getItemViewType(int position) {
        if(position%8==0&&position!=0)return USER_ICON;
        return VIDEO_LIST;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {//i指的是ViewType，用来显示多种类型
        if(i == VIDEO_LIST)
            return new ViewHolder4Video(LayoutInflater.from(mContext).inflate(R.layout.item_video,viewGroup,false));
        return new ViewHolder4User(LayoutInflater.from(mContext).inflate(R.layout.item_user_interested,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(position%8!=0||position==0){
            ViewHolder4Video viewHolder4Video = (ViewHolder4Video) holder;
            Glide.with(mContext).load(feedList.get(position).getImageUrl()).into(viewHolder4Video.im_icon);
            viewHolder4Video.tv_name.setText(feedList.get(position).getUserName());
            viewHolder4Video.tv_studentId.setText(feedList.get(position).getStudentId());
        }
    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }


    public class ViewHolder4Video extends RecyclerView.ViewHolder{

        ImageView im_icon;
        TextView tv_name;
        TextView tv_studentId;

        public ViewHolder4Video(@NonNull View itemView) {
            super(itemView);
            im_icon = itemView.findViewById(R.id.item_video_iv_icon);
            tv_name= itemView.findViewById(R.id.item_video_tv_name);
            tv_studentId = itemView.findViewById(R.id.item_video_tv_studentid);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener!=null){
                        onItemClickListener.OnItemClick(v,
                                feedList.get(getPosition()).getVideoURL(),
                                feedList.get(getPosition()).getUserName(),
                                feedList.get(getPosition()).getStudentId());
                    }
                }
            });
        }
    }

    public class ViewHolder4User extends RecyclerView.ViewHolder{

        CircleImageView icon1;
        CircleImageView icon2;
        CircleImageView icon3;

        public ViewHolder4User(@NonNull View itemView) {
            super(itemView);
            icon1 = itemView.findViewById(R.id.item_civ_icon1);
            icon2 = itemView.findViewById(R.id.item_civ_icon2);
            icon3 = itemView.findViewById(R.id.item_civ_icon3);
        }
    }

    /**
     * 点击RecyclerView某条的监听
     */
    public interface OnItemClickListener{
        /**
         * 当RecyclerView某条被点击时回调
         * @param view 点击item的视图
         * @param videoUrl 点击得到的数据
         */
        public void OnItemClick(View view,String videoUrl,String name,String id);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
