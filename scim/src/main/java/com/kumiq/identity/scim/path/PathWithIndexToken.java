package com.kumiq.identity.scim.path;

import com.kumiq.identity.scim.utils.TypeUtils;

import java.util.regex.Pattern;

/**
 * A path token with index component after the path
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class PathWithIndexToken extends SimplePathToken {

    private static final Pattern pattern = Pattern.compile("(.*?)\\[(.*?)\\]");

    private final String pathComponent;
    private final Integer indexComponent;

    public PathWithIndexToken(String pathWithIndex) {
        super(pathWithIndex);

        try {
            this.pathComponent = pathWithIndex.split("\\[")[0];
            this.indexComponent = Integer.parseInt(pathWithIndex.split("\\[")[1].split("]")[0]);
        } catch (Exception ex) {
            throw new IllegalArgumentException(pathWithIndex + " is not a valid path with index token");
        }
    }

    @Override
    public Object evaluate(Object cursor, Configuration configuration) {
        Object list = configuration.getObjectProvider().getPropertyValue(cursor, attributeName(configuration));

        if (list == null)
            return null;
        if (TypeUtils.asCollection(list).size() <= this.indexComponent)
            return null;

        return configuration.getObjectProvider().getArrayIndex(list, this.indexComponent);
    }

    public String getPathComponent() {
        return pathComponent;
    }

    @Override
    public String queryFreePath() {
        return this.getPathComponent();
    }

    @Override
    public boolean isPathWithIndex() {
        return true;
    }

    @Override
    public boolean isSimplePath() {
        return false;
    }

    public Integer getIndexComponent() {
        return indexComponent;
    }
}
