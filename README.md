# 编译原理-Code
## 语言简述
* 采用java编写,IDE为Intellij IDEA 
* 我自己定义的这个语言为加入了分号以及括号的Python,众所周知,Python有着极为严格的格式要求,有时候一个空格的错误就会导致无法运行,我自己"定义"(魔改)的这门语言取消了
如此严格的要求,使得其格式更加灵活,故语法与Python类似
## 词法分析
* 为了简化正则,我的正则里面元素采用的集合的形式,即一个元素代表一组元素,例如 `a -> [a~z]|[A~Z]`
* 我采用的是正则表达式直接转到DFA,故没有NFA这一步
* 首先是根据正则表达式构建抽象语法书,这里龙书上说的很详细便不再赘述
* 然后根据语法树构建各个参数,最后可以得到DFA,DFA我是用的一个List表示,详情请看相关注释