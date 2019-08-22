call mvn package
cd target
move cn.yanglj65.alibaba-jar-with-dependencies.jar I:\Desktop\KuQ\data\app\com.sobte.cqp.jcq\app
cd I:\Desktop\KuQ\data\app\com.sobte.cqp.jcq\app
del cn.yanglj65.alibaba.jar
echo "delete successfully"
rename cn.yanglj65.alibaba-jar-with-dependencies.jar cn.yanglj65.alibaba.jar
echo "rename successfully"
pause