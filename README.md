 # TVProjectUtils (以后再遇到tv项目的时候，再有问题，也会继续更新)
 
## gradle  [ ![Download](https://api.bintray.com/packages/yan157/maven/tvprojectutils/images/download.svg) ](https://bintray.com/yan157/maven/tvprojectutils/_latestVersion) ↘
compile 'com.yan:tvprojectutils:(↖)'
 ###### 外包，难免碰到tv的项目，以下是个人在项目中遇到问题后，给出的三个类
 ### 1.[FocusRecyclerView](tvprojectutils/src/main/java/com/yan/tvprojectutils/FocusRecyclerView.java)
 解决recyclerView的焦点问题,不需要对layoutManager做任何修改，之前一直拘泥于layoutManager来处理焦点飞的问题（主要网上查查，都是这么搞的），结果发现，越改问题越多，最终还是决定从recyclerView入手，现在个人写的tv项目都是用的这个来做的焦点适配，目前运行稳定。
 ### 1.[MarqueeText](tvprojectutils/src/main/java/com/yan/tvprojectutils/MarqueeText.java)
 一个是只有焦点在textView上的时候TextView自带的Marquee才会起作用，二个是上一个项目需要做超出用"..."来表示，被选中时正常滚动，所以重写了TextView来实现
 ### 1.[DensityHelper](tvprojectutils/src/main/java/com/yan/tvprojectutils/DensityHelper.java)
 DensityHelper来自于[RudeAdaptDemo](https://github.com/Firedamp/RudeAdaptDemo)， 感谢作者提供这么好的一个思路来做屏幕适配。
 <br/>
 之前一直用的是autolayout，可惜鸿神暂时不维护了，使用上有一定的风险，而DensityHelper使用及其简单，目前适配正常。
 