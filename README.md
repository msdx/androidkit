androidkit
==========



- android开发工具包，灵活小巧，低侵入，帮助提高android应用开发效率。
- 基于android 1.6，通用于各android项目。

使用范例
-
**UIBind**

        // 这里添加注解，指定对应的id
        @AndroidView(id = R.id.home_result_upload)
        private TextView mTextUpload;
        // 资源的绑定，指定id，类型
        @AndroidRes(id = R.string.result_scan, type = ResType.STRING)
        private String mStringScan;
        // 对AdapterView的子类还可以绑定onCreateContextMenu，onItemClick等的事件监听。
        @AndroidView(id = R.id.user_listView, onCreateContextMenu = "listViewContextMenu", onItemClick = "onListItemClick")
        private ListView mUserListView;
 
       @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                // 调用此方法将对控件、事件进行绑定
                UIBindUtil.bind(this, R.layout.activity_home);
                // 调用此方法将对资源如String, StringArray, Drawable等资源对象进行绑定。
                ResBindUtil.bindAllRes(this);
        }
 

       // 这里对应着上面的mUserListView的onCreateContextMenu方法名。
        public void listViewContextMenu(ContextMenu menu, View v,
                        ContextMenuInfo menuInfo) {
                menu.add(0, DELETE, 1, "删除");
        }
 

       // 这里对应着上面的mUserListView的onItemClick方法名。
        public void onListItemClick(AdapterView<?> arg0, View arg1, int arg2,
                        long arg3) {
                mUserListView.showContextMenuForChild(arg1);
        }
 

       // 对View的setOnClickListener事件进行绑定，这样不再需要先声明变量。
        @OnClick(viewId = { R.id.home_scan, R.id.home_upload_result,
                        R.id.home_user_manager })
        public void onButtonClick(View v) {
                switch (v.getId()) {
                case R.id.home_scan:
                        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                        this.startActivityForResult(intent, HOME_ACTIVITY);
                        break;
                case R.id.home_upload_result:
                        break;
                case R.id.home_user_manager:
                        startActivity(new Intent(this, UserManagerActivity.class));
                        break;
                default:
                        break;
                }
        }
 
