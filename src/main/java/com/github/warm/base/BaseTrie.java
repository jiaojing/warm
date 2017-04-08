package com.github.warm.base;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class BaseTrie {
    private static final String MAIN_DICT = "/dictionary.txt";

    private TrieNode root = TrieNode.createRoot();

    public BaseTrie() {
        loadDict();
    }

    public void loadDict() {
        try (
                InputStream is = this.getClass().getResourceAsStream(MAIN_DICT);
                BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")))
        ) {

            while (br.ready()) {
                String line = br.readLine();
                // 匹配多个空格或者制表符
                String[] tokens = line.split("[\t ]+");

                if (tokens.length < 2) {
                    continue;
                }

                String word = tokens[0];
                insertTrie(word);
            }
        } catch (Exception e) {

        }

    }

    /**
     * @param word
     */
    void insertTrie(String word) {
        char[] wordChar = word.toCharArray();
        TrieNode next = root;
        for (int i = 0; i < wordChar.length; i++) {
            char key = wordChar[i];
            next = next.putIfAbsent(key);
            if (i == (wordChar.length - 1)) {
                next.markAsWord();
            }
        }
    }

    /**
     * 每次右移,一个字符,把以当前字符开始匹配到的所有词,都收集
     *
     * @param text
     */
    public List<String> maxWords(String text) {
        List<String> result = new ArrayList<>();

        char[] chars = text.toCharArray();
        for (int i = 0; i < chars.length; i++) {

            //从当前位置,往后匹配
            List<Integer> positions = etractToken(chars, i);
            //收集所有匹配到的词
            for (int position : positions) {
                result.add(new String(chars, i, position - i));
            }
            // 往后挪一位,继续重复操作.
        }
        return result;
    }

    /**
     * 从右到左,收集匹配的所有词.然后跳转到最大词的位置,进行下一轮.
     *
     * @param text
     * @return
     */
    public List<String> maxLenWords(String text) {
        List<String> result = new ArrayList<>();

        char[] chars = text.toCharArray();
        int offset = 0;
        while (offset < chars.length) {
            List<Integer> positions = etractToken(chars, offset);

            // 如果没有匹配,往后挪一格
            if (positions.size() == 0) {
                offset++;
                continue;
            }


            //收集匹配到的所有词
            for (int position : positions) {
                result.add(new String(chars, offset, position - offset));
            }

            //挪动到最长词的位置,继续下一轮匹配.
            int maxP = positions.get(positions.size() - 1);
            offset = maxP;
        }
        return result;
    }

    /**
     * 从右到左,收集匹配的最长词,然后跳转到最长词位置,进行下一轮.
     *
     * @param text
     * @return
     */
    public List<String> maxWordsLen(String text) {
        List<String> result = new ArrayList<>();

        char[] chars = text.toCharArray();
        int offset = 0;
        while (offset < chars.length) {
            List<Integer> positions = etractToken(chars, offset);

            // 如果没有匹配,往后挪一格
            if (positions.size() == 0) {
                offset++;
                continue;
            }

            //收集匹配的最长词
            int maxP = positions.get(positions.size() - 1);
            result.add(new String(chars, offset, maxP - offset));

            //挪动到最长词的位置,继续下一轮匹配.
            offset = maxP;
        }
        return result;
    }

    private List<Integer> etractToken(char[] chars, int offset) {
        List<Integer> positions = new ArrayList<>();
        int maxPosition = chars.length - 1;
        TrieNode next = this.root;
        while (offset <= maxPosition) {
            char key = chars[offset];
            TrieNode node = next.lookfor(key);
            if (node == null) {
                break;
            } else {
                next = node;
                offset++;
                if (node.isWord()) {
                    positions.add(offset);
                }
            }
        }

        return positions;
    }
}



