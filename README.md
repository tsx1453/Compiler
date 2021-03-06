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
* 最后词法分析器我是先构建好各个DFA,然后先一个字符一个字符往后扫描,将符号和字符串分开,符号直接生成token,字符串通过正则表达式判定种类以及正误,判断类型的先后顺序是 `符号(字符串)->保留字->标识符(数字)`,(括号里面等同于平级,级在同一阶段检测)除了这几类,我单独有一个类型字符串,因为发现和标识符放一起不太对,但是通过正则又不好写,因为字符串接受一切类型,于是在扫描时通过其 `'字符串'` 特殊结构直接将其生成token,对于注释的判定,只规定了单行注释即 `#`,在生成token时直接忽略

#### 流程
`reText.java获取正则表达式` --> `stringAddSomething.java添加cat节点` --> `lexicalTree.java构建抽象语法树` -->
`DFA.java构建DFA` --> `minDfa.java最小化DFA` --> `LexicalAnalysis.java词法分析并生成token序列`
#### 使用方法
直接调用`LexicalAnalysis`类的`LexcalAnalysis()`方法即可，其参数为源代码的位置，会在同级生成xxx.token的保存着token序列的文本文件

## 语法分析
### LL1
* 消除左递归,提取左公因子
* 得到first,follow集合
* 得到分析表
#### 使用
调用`Ll1SuntaxAnalysis`类的`analysis`方法即可;
### LR0
* 根据文法构建DFA
* 根据DFA得到分析表
#### 使用
调用`LR0SyntaxAnalysis`类的`analysi`方法
## 文件结构

```文件结构:
│  .gitignore
│  CompilerCoding.iml
│  test.pt //自定义的语言的一个测试样例
└─src
    ├─LexicalAnalysisPackage
    │      DFA.java  DFA的类
    │      draw.java DFA可视化
    │      Graph.java  DFA的图
    │      LexicalAnalysis.java 最终词法分析的类
    │      lexicalTree.java 抽象语法树
    │      minDfa.java 最小化
    │      reText.java 正则表达式
    │      stringAddSomething.java 添加cat节点
    │      Token.java
    │
    ├─SyntaxAnalysisPackage
    │  Grammar.java 保存文法的类
    │  │
    │  ├─LL1
    │  │      FirstAndFollows.java 
    │  │      ForecastAnalysisTable.java 分析表
    │  │      GrammarProcess.java 处理文法,消除左递归,提取左公因子
    │  │      Ll1SuntaxAnalysis.java 语法分析的主类
    │  │
    │  └─LR0
    │         DFA.java
    │         GrammerDFA.java 
    │         LR0SyntaxAnalysis.java
    │         LR0SyntaxAnalysisTable.java
    │
    └─Tools
            myIoClass.java 输入输出类
            MyTools.java 一些需要用到的工具的类
```
主要方法均在龙书以及相应代码的注释中,如有错误以及不足欢迎指出.
