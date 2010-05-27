package wesnoth_eclipse_plugin.preferences;

import java.io.File;

import org.eclipse.core.runtime.Path;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import wesnoth_eclipse_plugin.Activator;

/**
 * This class represents a preference page that
 * is contributed to the Preferences dialog. By
 * subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows
 * us to create a page that is small and knows how to
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They
 * are stored in the preference store that belongs to
 * the main plug-in class. That way, preferences can
 * be accessed directly via the preference store.
 */
public class WesnothEditorPreferences
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	private DirectoryFieldEditor wmlToolsField;

	public WesnothEditorPreferences() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Wesnoth User-Made-Content Plugin preferences");
	}

	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	@Override
	public void createFieldEditors() {
		addField(new FileFieldEditor(PreferenceConstants.P_WESNOTH_EXEC_PATH,
				"Wesnoth executable path:", getFieldEditorParent()));
		addField(new  DirectoryFieldEditor(PreferenceConstants.P_WESNOTH_WORKING_DIR,
				"Working directory:", getFieldEditorParent()));
		addField(new  DirectoryFieldEditor(PreferenceConstants.P_WESNOTH_USER_DIR,
				"User data directory:", getFieldEditorParent()));

		wmlToolsField = new  DirectoryFieldEditor(PreferenceConstants.P_WESNOTH_WMLTOOLS_DIR,
				"WML* tools directory:", getFieldEditorParent());
		addField(wmlToolsField);
	}

	public void init(IWorkbench workbench) {
	}

	@Override
	protected void checkState()
	{
		super.checkState();
		setValid(false);

		if (!(new File(wmlToolsField.getStringValue() + Path.SEPARATOR + "wmllint").exists()))
		{
			setErrorMessage("wmllint cannot be found in the wml tools path");
			return;
		}

		setErrorMessage(null);
		setValid(true);
	}

	@Override
	public void propertyChange(PropertyChangeEvent event)
	{
		super.propertyChange(event);
		if (event.getProperty().equals(FieldEditor.VALUE))
			checkState();
	}
}