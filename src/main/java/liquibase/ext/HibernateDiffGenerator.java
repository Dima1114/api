package liquibase.ext;

import liquibase.diff.DiffResult;
import liquibase.diff.Difference;
import liquibase.diff.ObjectDifferences;
import liquibase.diff.compare.CompareControl;
import liquibase.diff.core.StandardDiffGenerator;
import liquibase.exception.DatabaseException;
import liquibase.snapshot.DatabaseSnapshot;
import liquibase.structure.AbstractDatabaseObject;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.Index;
import liquibase.structure.core.Table;
import liquibase.structure.core.UniqueConstraint;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class HibernateDiffGenerator extends StandardDiffGenerator {

    private final String[] ignoringTables = new String[]{
            "qrtz_blob_triggers",
            "qrtz_calendars",
            "qrtz_cron_triggers",
            "qrtz_fired_triggers",
            "qrtz_job_details",
            "qrtz_locks",
            "qrtz_paused_trigger_grps",
            "qrtz_scheduler_state",
            "qrtz_simple_triggers",
            "qrtz_simprop_triggers",
            "qrtz_triggers"};

    @Override
    public int getPriority() {
        return 10;
    }

    @Override
    public DiffResult compare(DatabaseSnapshot referenceSnapshot, DatabaseSnapshot comparisonSnapshot, CompareControl compareControl) throws DatabaseException {
        DiffResult diffResult = super.compare(referenceSnapshot, comparisonSnapshot, compareControl);

        // Check Indexes
        checkConstraints(diffResult, Index.class, "unique");
        // Check Constraints
        checkConstraints(diffResult, UniqueConstraint.class, "clustered");

        // Check ignoring tables
        Set<? extends DatabaseObject> unexpectedObjects = diffResult.getUnexpectedObjects();

        unexpectedObjects.removeIf(databaseObject -> isIgnoringTables(databaseObject.getName()) || isIgnoringByAttribute(databaseObject));

        return diffResult;
    }

    private boolean isIgnoringByAttribute(DatabaseObject databaseObject) {
        Set<String> names = databaseObject.getAttributes();

        Set<?> attributes = names.stream()
                .map(it -> databaseObject.getAttribute(it, null))
                .collect(Collectors.toSet());

        Set<Table> tables = attributes.stream()
                .filter(it -> it instanceof Table)
                .map(it -> (Table) it)
                .collect(Collectors.toSet());

        return !tables.isEmpty() && tables.stream().map(Table::getName).anyMatch(this::isIgnoringTables);
    }

    private boolean isIgnoringTables(String name) {
        return Arrays.stream(ignoringTables).anyMatch(name::equalsIgnoreCase);
    }

    private void checkConstraints(DiffResult diffResult, Class<? extends AbstractDatabaseObject> clazz, String type) {

        diffResult.getChangedObjects(clazz).forEach((key, value) -> {
            if (differencesEquals(value, type)) {
                diffResult.getChangedObjects().remove(key);
            }
        });
    }

    private boolean differencesEquals(ObjectDifferences objectDifferences, String type) {
        Set<Difference> differences = new HashSet<>(objectDifferences.getDifferences());

        //unique = null in hibernate equals unique = false in mysql
        differences.removeIf(difference -> type.equalsIgnoreCase(difference.getField()) && difference.getReferenceValue() == null &&
                Boolean.FALSE.equals(difference.getComparedValue()));

        return differences.isEmpty();
    }
}
