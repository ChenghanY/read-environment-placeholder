package com.james.reader;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO 循环整个文件夹
 */
public class PlaceHolderReader {

    public static void main(String[] args) throws IOException {
        File file = new File("d:" + File.separator + "root" + File.separator + "application.yml");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        Pattern pattern = Pattern.compile("(?=\\$\\{)(\\$\\{)(.*?)(?=})(})");
        while((line=br.readLine())!=null){
            // 清理掉前置空白符
            line = line.replaceAll(" ", "");
            // 正则提取形如 ${} 的占位符
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                String original = matcher.group();
                String content = matcher.group(2);
                String remote = "无";
                String local = " 无";
                if (content.contains(":")) {
                    String[] split = content.split(":");
                    remote = split[0];
                    local = content.replace(remote + ":", "");
                }

                System.out.println(line + "\t" + original + "\t" + content + "\t" + remote + "\t" + local);
            }
        }

    }
}
