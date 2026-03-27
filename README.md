**项目描述**
-基于 Jetpack Compose 开发的 Android 股票行情与财经资讯应用。提供实时股票检索、K线图表分析、自选股本地持久化管理，以及支持下拉刷新与懒加载的全球财经新闻流。

**技术细节**

-架构模式: MVVM。通过 ViewModel 与 AndroidViewModel 分离业务逻辑与 UI 状态，确保数据单向流动 (UDF)。

-UI 框架: 全面采用 Jetpack Compose (Material 3)。使用 NavigationSuiteScaffold 构建主导航，基于 LazyColumn 与 PullToRefreshBox 实现新闻列表的高性能懒加载与下拉刷新体验。

-网络层: Retrofit2 + Gson。采用多实例配置（独立拆分股票 API 与 Finnhub 新闻 API）。利用 Kotlin Coroutines 的 async/awaitAll 机制实现多只自选股的并发网络请求。

-本地存储: Room Database + KSP 编译。使用 Flow 观察本地数据库的变动，实现“自选股添加/移除 -> 数据库更新 -> 首页列表自动拉取新行情”的响应式数据驱动闭环。

-异步处理: 纯 Kotlin Coroutines 处理后台线程与延时任务，通过 LaunchedEffect 绑定 UI 生命周期触发初始数据拉取。

-多媒体加载: 接入 Coil (AsyncImage) 实现网络图片的异步获取与按需裁剪渲染
