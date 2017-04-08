package com.github.warm.base;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by tony on 08/04/2017.
 */
public class TrieNode {

    public final char value;
    private int state = 0;
    private Map<Character, TrieNode> children = new TreeMap<>();

    public TrieNode(char value) {
        this.value = value;
    }

    public TrieNode markAsWord() {
        this.state = 1;
        return this;
    }

    public boolean isWord() {
        return this.state == 1;
    }

    public TrieNode putIfAbsent(char key) {
        return this.children.computeIfAbsent(key, TrieNode::new);
    }

    public TrieNode lookfor(char key) {
        return this.children.get(key);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("| --> " + this.value + "\n");
        for (TrieNode node : children.values()) {
            sb.append(node.toString());
        }
        return sb.toString();
    }

    public static TrieNode createRoot() {
        return new TrieNode('\0');
    }

}

