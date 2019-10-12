### 倒入3方
1.Add it in your root build.gradle
```java
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
2.加入jar包
```java
dependencies {
	        implementation 'com.github.YwcSillyWhite:purewhileadapter:Tag'
	}
```
3.初始化<br>
初始化的作用在加载更多判断了是否网络可以用,如果没有初始化，网络都是可用状态
```java
 AdapterUtils.initAdapter(this);
```

4.使用<br>
书写适配器继承baseadapter或者basemoreadapter

### 使用
###### baseAdapter是基础类， baseMoreAdapter 是baseAdapter子类
1.数据处理
```java
baseAdapter 方法
flush()  刷新
addData() 添加
removePosition（）删除
clear（）清空

baseMoreAdapter
//参数1 网络状态，参数2 是否刷新，参数3 数据
finishStatus(boolean network,boolean flush,List<T> list)
```

2.itemview点击事件（单点本身允许，双点本身不允许）
```java
baseAdapter 源码
    //点击事件
    private OnItemListener onItemListener;
    //itemview是否点击
    private boolean isItemViewClick=true;
    public void setOnItemListener(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }
    private OnItemLongListener onItemLongListener;
    private boolean isItemViewLongClick=false;
    public void setOnItemLongListener(OnItemLongListener onItemLongListener) {
        this.onItemLongListener = onItemLongListener;
    }
		
	//接口	
	public interface OnItemListener {
	   //参数1 adapter 参数2 data哪个position，参数3  是否是itemview点击
    void onClick(RecyclerView.Adapter adapter,int position,boolean itemView);
 }
```

3.添加头尾
```java
addHead() 添加头部
removeHead（） 删除头部

addFoot()添加尾部
removeFoot()删除尾部
```

4.添加加载更多
```java
mainAdapter.setOnLoadListener(new OnLoadListenerImp() {
            //加载更多
            @Override
            public void judgeLoad() {
                page++;
                flush(false,page>5?9:10);
            }

            //加载更多判断
            @Override
            public boolean judge() {
                return super.judge();
            }

            //点击没有网络重新加载
            @Override
            public void againLoad() {
                super.againLoad();
            }
        });
```

5.添加全局布局（前期加载布局）
```java
mainAdapter.setFullStatus(FullView.LOAD,false);
        mainAdapter.setOnFullListener(new OnFullListener() {
				 //点击没有网络重新加载
            @Override
            public void againLoad() {
                flush(false,10);
            }
        });
```

6.设置自己loadview和fullview布局
```java
   public void setLoadView(LoadView loadView) {
        if (loadView != null)
            this.loadView=loadView;
    }

    public void setFullView(FullView fullView) {
        if (fullView!=null)
            this.fullView = fullView;
    }
```

7.添加布局
```java
        addLayoutId(R.layout.adapter_one);
```




