package com.xxx;

import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 解析xml文件，把id和class的属性对应关系放到集合中(list)
 * 通过反射得到实例化对象（遍历list放在map中）
 * 通过id属性值获取指定的实例化对象
 */
public class MyClassPathXmlApplicationContext implements MyFactory{


    private List<MyBean> beanList = new ArrayList<>();//存放从配置文件中获取到的bean标签信息
    private Map<String,Object> beanMap = new HashMap<>();//存放实例化好的对象，通过id获取对应的对象

    // 通过带参构造器得到对应的配置文件
    public MyClassPathXmlApplicationContext(String fileName) throws DocumentException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        //dom4j解析xml文件，得到list集合
        this.parseXml(fileName);

        //通过反射得到对应的实例化对象，放在map中
        this.instanceBean();
    }

    //通过反射得到对应的实例化对象，放置在Map对象中
    private void instanceBean() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        //判断对象集合是否为空
        if (!beanList.isEmpty()){
            //不为空遍历集合获取对应的id和class属性
            for (MyBean myBean : beanList){
                String id = myBean.getId();
                String clazz = myBean.getClazz();
                //通过类的全路径名 反射 得到实例化对象 Class.forName().newInstance()
                Object object = Class.forName(clazz).newInstance();
                beanMap.put(id,object);
            }
        }

        //将对应的id和实例化好的bean对象设置到map中
    }

    private void parseXml(String fileName) throws DocumentException {
        //获取解析器
        SAXReader saxReader = new SAXReader();
        //获取配置文件的url
        URL url = this.getClass().getClassLoader().getResource(fileName);
        //通过解析器解析xml文件
        Document document = saxReader.read(url);
        //通过xpath语法解析，获取beans标签下的所有bean标签
        XPath xPath = document.createXPath("beans/bean");
        //解析文档对象后，返回元素集合
        List<Node> nodesList = xPath.selectNodes(document);

        //判空
        if (!nodesList.isEmpty()){
            //不为空遍历集合
            for (Node node : nodesList){
                if (node instanceof Element){
                    //获取bean标签元素的属性（id和和class属性值）
                    String id = ((Element) node).attributeValue("id");
                    String clazz = ((Element) node).attributeValue("class");
                    MyBean myBean = new MyBean();
                    myBean.setId(id);
                    myBean.setClazz(clazz);
                    beanList.add(myBean);
                }
            }
        }
    }

    /**
     * 通过id获取实例化后的bean对象
     * @param id
     * @return
     */
    @Override
    public Object getBean(String id) {
        return beanMap.get(id);
    }
}
