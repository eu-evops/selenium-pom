package uk.sponte.automation.seleniumpom.stolen;

import org.openqa.selenium.By;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.pagefactory.ByAll;
import org.openqa.selenium.support.pagefactory.ByChained;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.HashSet;

import static org.openqa.selenium.support.How.CLASS_NAME;

/**
 * Created by evops on 02/02/2016.
 */
public class Annotations {

    private final String name;
    private AnnotatedElement field;

    public Annotations(AnnotatedElement field, String name) {
        this.field = field;
        this.name = name;
    }

    public boolean isLookupCached() {
        return this.field.getAnnotation(CacheLookup.class) != null;
    }

    public By buildBy() {
        this.assertValidAnnotations();
        By ans = null;
        FindBys findBys = (FindBys) this.field.getAnnotation(FindBys.class);
        if (findBys != null) {
            ans = this.buildByFromFindBys(findBys);
        }

        FindAll findAll = (FindAll) this.field.getAnnotation(FindAll.class);
        if (ans == null && findAll != null) {
            ans = this.buildBysFromFindByOneOf(findAll);
        }

        FindBy findBy = (FindBy) this.field.getAnnotation(FindBy.class);
        if (ans == null && findBy != null) {
            ans = this.buildByFromFindBy(findBy);
        }

        if (ans == null) {
            ans = this.buildByFromDefault();
        }

        if (ans == null) {
            throw new IllegalArgumentException("Cannot determine how to locate element " + this.field);
        } else {
            return ans;
        }
    }

    protected By buildByFromDefault() {
        return new ByIdOrName(this.name);
    }

    protected By buildByFromFindBys(FindBys findBys) {
        this.assertValidFindBys(findBys);
        FindBy[] findByArray = findBys.value();
        By[] byArray = new By[findByArray.length];

        for (int i = 0; i < findByArray.length; ++i) {
            byArray[i] = this.buildByFromFindBy(findByArray[i]);
        }

        return new ByChained(byArray);
    }

    protected By buildBysFromFindByOneOf(FindAll findBys) {
        this.assertValidFindAll(findBys);
        FindBy[] findByArray = findBys.value();
        By[] byArray = new By[findByArray.length];

        for (int i = 0; i < findByArray.length; ++i) {
            byArray[i] = this.buildByFromFindBy(findByArray[i]);
        }

        return new ByAll(byArray);
    }

    protected By buildByFromFindBy(FindBy findBy) {
        this.assertValidFindBy(findBy);
        By ans = this.buildByFromShortFindBy(findBy);
        if (ans == null) {
            ans = this.buildByFromLongFindBy(findBy);
        }

        return ans;
    }

    protected By buildByFromLongFindBy(FindBy findBy) {
        How how = findBy.how();
        String using = findBy.using();
        switch (how) {
            case CLASS_NAME:
                return By.className(using);
            case CSS:
                return By.cssSelector(using);
            case ID:
                return By.id(using);
            case ID_OR_NAME:
                return new ByIdOrName(using);
            case LINK_TEXT:
                return By.linkText(using);
            case NAME:
                return By.name(using);
            case PARTIAL_LINK_TEXT:
                return By.partialLinkText(using);
            case TAG_NAME:
                return By.tagName(using);
            case XPATH:
                return By.xpath(using);
            default:
                throw new IllegalArgumentException("Cannot determine how to locate element " + this.field);
        }
    }

    protected By buildByFromShortFindBy(FindBy findBy) {
        return !"".equals(findBy.className()) ? By.className(findBy.className()) : (!"".equals(findBy.css()) ? By.cssSelector(findBy.css()) : (!"".equals(findBy.id()) ? By.id(findBy.id()) : (!"".equals(findBy.linkText()) ? By.linkText(findBy.linkText()) : (!"".equals(findBy.name()) ? By.name(findBy.name()) : (!"".equals(findBy.partialLinkText()) ? By.partialLinkText(findBy.partialLinkText()) : (!"".equals(findBy.tagName()) ? By.tagName(findBy.tagName()) : (!"".equals(findBy.xpath()) ? By.xpath(findBy.xpath()) : null)))))));
    }

    private void assertValidAnnotations() {
        FindBys findBys = (FindBys) this.field.getAnnotation(FindBys.class);
        FindAll findAll = (FindAll) this.field.getAnnotation(FindAll.class);
        FindBy findBy = (FindBy) this.field.getAnnotation(FindBy.class);
        if (findBys != null && findBy != null) {
            throw new IllegalArgumentException("If you use a \'@FindBys\' annotation, you must not also use a \'@FindBy\' annotation");
        } else if (findAll != null && findBy != null) {
            throw new IllegalArgumentException("If you use a \'@FindAll\' annotation, you must not also use a \'@FindBy\' annotation");
        } else if (findAll != null && findBys != null) {
            throw new IllegalArgumentException("If you use a \'@FindAll\' annotation, you must not also use a \'@FindBys\' annotation");
        }
    }

    private void assertValidFindBys(FindBys findBys) {
        FindBy[] arr$ = findBys.value();
        int len$ = arr$.length;

        for (int i$ = 0; i$ < len$; ++i$) {
            FindBy findBy = arr$[i$];
            this.assertValidFindBy(findBy);
        }

    }

    private void assertValidFindAll(FindAll findBys) {
        FindBy[] arr$ = findBys.value();
        int len$ = arr$.length;

        for (int i$ = 0; i$ < len$; ++i$) {
            FindBy findBy = arr$[i$];
            this.assertValidFindBy(findBy);
        }

    }

    private void assertValidFindBy(FindBy findBy) {
        if (findBy.how() != null && findBy.using() == null) {
            throw new IllegalArgumentException("If you set the \'how\' property, you must also set \'using\'");
        } else {
            HashSet finders = new HashSet();
            if (!"".equals(findBy.using())) {
                finders.add("how: " + findBy.using());
            }

            if (!"".equals(findBy.className())) {
                finders.add("class name:" + findBy.className());
            }

            if (!"".equals(findBy.css())) {
                finders.add("css:" + findBy.css());
            }

            if (!"".equals(findBy.id())) {
                finders.add("id: " + findBy.id());
            }

            if (!"".equals(findBy.linkText())) {
                finders.add("link text: " + findBy.linkText());
            }

            if (!"".equals(findBy.name())) {
                finders.add("name: " + findBy.name());
            }

            if (!"".equals(findBy.partialLinkText())) {
                finders.add("partial link text: " + findBy.partialLinkText());
            }

            if (!"".equals(findBy.tagName())) {
                finders.add("tag name: " + findBy.tagName());
            }

            if (!"".equals(findBy.xpath())) {
                finders.add("xpath: " + findBy.xpath());
            }

            if (finders.size() > 1) {
                throw new IllegalArgumentException(String.format("You must specify at most one location strategy. Number found: %d (%s)", new Object[]{Integer.valueOf(finders.size()), finders.toString()}));
            }
        }
    }
}
