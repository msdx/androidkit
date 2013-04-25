androidkit
==========



- android开发工具包，灵活小巧，低侵入，帮助提高android应用开发效率。
- 基于android 1.6，通用于各android项目。

使用范例
-
**1.UI绑定模块**



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
        @OnClick(viewId = { R.id.home_scan, R.id.home_user_manager })
        public void onButtonClick(View v) {
            switch (v.getId()) {
            case R.id.home_scan:
                break;
            case R.id.home_user_manager:
                startActivity(new Intent(this, UserManagerActivity.class));
                break;
            default:
                break;
            }
        }
 
**2.HTTP模块**

目前只封装了简单的HTTP请求功能，提供了GET/POST/PUT/DELETE等相关的静态方法调用，并尽量让代码更简洁。

示例：

不带参数并且阻塞式的请求：

	    try {
            String result = Http.get(uri);
            System.out.println(result);
	    } catch (IOException e) {
            e.printStackTrace();
	    }

如果不想自己处理异常，这里还提供让你代码更简洁的方法：

	    String reslut = Http.getIgnoreException(uri);
	    System.out.println(reslut);

当然，更高版本的android系统是要求必须在非UI线程中进行网络访问的操作的，而且为了有更好的用户体验，我也建议用异步方式：

		HttpListener l = new HttpListener() {         
		    @Override
		    public void onFinish(String arg0) {
	            System.out.println(arg0);
		    }
		    
		    @Override
		    public void onFailed(String arg0) {
	            System.out.println("error:" + arg0);
	    	}
	    };
	    Http.getOnAsyn("http://bbs.gdou.edu.cn/", l);

带参数的POST请求：

		BasicParams params = new BasicParams();
        params.put(paramName, paramValue);
        try {
            return Http.post(uri, params);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;