package com.github.fsnippets.valhighlight.annotator;

import static com.intellij.lang.annotation.HighlightSeverity.INFORMATION;
import static com.intellij.openapi.editor.DefaultLanguageHighlighterColors.KEYWORD;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.psi.PsiAnnotatedJavaCodeReferenceElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiJavaToken;
import org.jetbrains.annotations.NotNull;

public class ValHighlightAnnotator implements Annotator {
    private static final String IMPORTED_VAL_REFERENCE = "val";
    private static final String FULL_LOMBOK_VAL = "lombok." + IMPORTED_VAL_REFERENCE;

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof PsiJavaToken && isStaticImportedValReference(element)) {
            markAsKeyword(holder);
        }
    }

    private boolean isStaticImportedValReference(PsiElement element) {
        if (!element.textMatches(IMPORTED_VAL_REFERENCE) || !(element.getParent() instanceof PsiAnnotatedJavaCodeReferenceElement)) {
            return false;
        }

        return FULL_LOMBOK_VAL.equals(((PsiAnnotatedJavaCodeReferenceElement) element.getParent()).getCanonicalText())
            && element.getContext() != null
            && element.getContext().getFirstChild() == element
            && element.getPrevSibling() == null;
    }

    private void markAsKeyword(@NotNull AnnotationHolder annotationHolder) {
        annotationHolder
            .newSilentAnnotation(INFORMATION)
            .enforcedTextAttributes(
                EditorColorsManager
                    .getInstance()
                    .getGlobalScheme()
                    .getAttributes(KEYWORD)
                )
            .create();
    }
}
