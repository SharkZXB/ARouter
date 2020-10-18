package com.sharkz.annotation_compiler;

import com.google.auto.service.AutoService;
import com.sharkz.annotation.Route;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

/**
 * ================================================
 * 作    者：SharkZ
 * 邮    箱：229153959@qq.com
 * 创建日期：2020/10/18  11:14
 * 描    述
 * 修订历史：
 * ================================================
 */
@AutoService(Processor.class) // 注册注解处理器到虚拟机
public class Process extends AbstractProcessor {

    Filer filer; // 文件操作

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
    }

    /**
     * 返回支持的Java版本
     *
     * @return
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return processingEnv.getSourceVersion();
    }

    /**
     * 添加 目标注解
     *
     * @return
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new HashSet<>();
        types.add(Route.class.getCanonicalName());
        return types;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        //生成文件代码
        Set<? extends Element> elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(Route.class);
        Map<String, String> map = new HashMap<>();
        for (Element element : elementsAnnotatedWith) {
            // TypeElement
            // VariableElement
            TypeElement typeElement = (TypeElement) element;
            //com.ysten.fuxi01.MainActivity
            String className = typeElement.getQualifiedName().toString();
            String pathName = typeElement.getAnnotation(Route.class).path();
            map.put(pathName, className + ".class");
        }
        if (map.size() == 0) {
            return false;
        }

        //
        Writer writer = null;
        //
        String className = "ActivityUtils" + System.currentTimeMillis();
        try {
            JavaFileObject classFile = filer.createSourceFile("com.shark.router." + className);
            writer = classFile.openWriter();
            writer.write("package com.shark.router;\n" +
                    "\n" +
                    "import com.sharkz.annotation_api.Arouter;\n" +
                    "import com.sharkz.annotation_api.IRouter;\n" +
                    "\n" +
                    "public class " + className + " implements IRouter {\n" +
                    "    @Override\n" +
                    "    public void putActivity() {\n");

            Iterator<String> iterator = map.keySet().iterator();
            while (iterator.hasNext()) {
                String activityKey = iterator.next();
                String cls = map.get(activityKey);
                writer.write("        Arouter.getInstance().putActivity(");
                writer.write("\"" + activityKey + "\"," + cls + ");");
            }
            writer.write("\n}\n" +
                    "}");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }
}

