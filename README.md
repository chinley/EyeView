# EyeView1_1
一款仿开眼的安卓软件

软件功能：
 1.每日精选：视频推荐 视频详情 横竖屏播放 手势设置（音量亮度增减）评论与点赞 收藏 分享
 2.往期分类：视频分类 
 3.软件社区：用户交流  分享视频
 4.用户管理：登录与注册 我的收藏 
 
关键技术：
 1. 底部菜单：fragment
 2. fragment、activity交互：Intent、SharedPreferences、bundle、forresult...
 3. 视频获取：
（1）联网获取json数据：AsyncHttpClient，HttpURLConnection
（2）多线程操作：Thread，handler进行线程通信
（3）解析数据：gson解析（有条件过滤）
 4. 网络图片显示：ImageLoader
 5. 网络视频播放：JZVideoPlayer
 6. 显示：ListView、BaseAdapter（利用convertView+ViewHolder重写getView()）
 7. 后台：Bmob后端云，存储用户信息、评论信息、分享信息
 
技术难点:
1.视频获取：
（1）联网获取json数据：AsyncHttpClient，HttpURLConnection
（2）多线程操作：Thread，handler进行线程通信
（3）解析数据：gson解析（有条件过滤）
2. 网络视频播放：JZVideoPlayer
3. fragment、activity交互：Intent、SharedPreferences、bundle、forresult...
