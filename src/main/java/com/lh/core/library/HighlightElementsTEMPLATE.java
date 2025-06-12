package com.lh.core.library;

public class HighlightElementsTEMPLATE {

    // Constant to hold the JavaScript code as a String
    public static final String HIGHLIGHT_SCRIPT = """
            targetSelector => {
                const elements = document.querySelectorAll(targetSelector);
                elements.forEach(element => {
                    const boundingBox = element.getBoundingClientRect();
                    const div = document.createElement('div');
                    div.style.position = 'absolute';
                    div.style.border = '2px solid red';
                    div.style.zIndex = '9999';
                    div.style.left = `${boundingBox.left + window.scrollX}px`;
                    div.style.top = `${boundingBox.top + window.scrollY}px`;
                    div.style.width = `${boundingBox.width}px`;
                    div.style.height = `${boundingBox.height}px`;
                    document.body.appendChild(div);
                });
            }
            """;

    // Private constructor to prevent instantiation
    private HighlightElementsTEMPLATE() {
        // Prevent instantiation
    }
}
