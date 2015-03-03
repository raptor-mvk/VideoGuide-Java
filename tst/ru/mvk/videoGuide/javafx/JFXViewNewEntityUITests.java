/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.videoGuide.javafx;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Test;
import ru.mvk.videoGuide.descriptor.ViewInfo;
import ru.mvk.videoGuide.descriptor.ViewInfoImpl;
import ru.mvk.videoGuide.descriptor.field.*;
import ru.mvk.videoGuide.javafx.field.*;
import ru.mvk.videoGuide.test.FieldValueTester;
import ru.mvk.videoGuide.test.Student;
import ru.mvk.videoGuide.utils.UITests;
import ru.mvk.videoGuide.view.View;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

public class JFXViewNewEntityUITests extends UITests<View<Student>> {
  @NotNull
  private final FieldValueTester<Boolean> saveButtonState = new FieldValueTester<>();
  @NotNull
  private final FieldValueTester<Boolean> cancelButtonState = new FieldValueTester<>();
  @NotNull
  private final Consumer<Boolean> saveButtonHandler = saveButtonState::setValue;
  @NotNull
  private final Runnable cancelButtonHandler = () -> cancelButtonState.setValue(true);
  @NotNull
  private final ViewInfo<Student> viewInfo = prepareViewInfo();
  @NotNull
  private final Student student = prepareStudent();

  @Test
  public void fieldLabelsShouldBeCorrect() {
    @NotNull View<Student> view = getObjectUnderTest();
    @NotNull Iterator<Entry<String, NamedFieldInfo>> iterator = viewInfo.getIterator();
    assertFieldLabelsAreCorrect(view, iterator);
  }

  @Test
  public void fieldValuesShouldBeCorrect() {
    @NotNull View<Student> view = getObjectUnderTest();
    @NotNull Iterator<Entry<String, NamedFieldInfo>> iterator = viewInfo.getIterator();
    assertFieldsHaveCorrectValues(view, iterator, student);
  }

  @Test
  public void fieldsShouldBeOfCorrectTypes() {
    @NotNull View<Student> view = getObjectUnderTest();
    @NotNull Iterator<Entry<String, NamedFieldInfo>> iterator = viewInfo.getIterator();
    @NotNull Map<String, Class<? extends Node>> nodeTypes = new HashMap<>();
    nodeTypes.put("id", NaturalField.class);
    nodeTypes.put("name", LimitedTextField.class);
    nodeTypes.put("gpa", RealField.class);
    nodeTypes.put("penalty", IntegerField.class);
    nodeTypes.put("graduated", CheckBoxField.class);
    nodeTypes.put("lecturesTime", DurationField.class);
    assertFieldsHaveCorrectTypes(view, iterator, nodeTypes);
  }

  @Test
  public void nodesTabOrderShouldBeCorrect() {
    @NotNull View<Student> view = getObjectUnderTest();
    @NotNull Iterator<Entry<String, NamedFieldInfo>> iterator = viewInfo.getIterator();
    assertNodesHaveCorrectTabOrder(view, iterator);
  }

  @Test
  public void clickSave_ShouldCallSaveButtonHandlerWithTrueParameter() {
    @NotNull View<Student> view = getObjectUnderTest();
    @NotNull String saveButtonId = view.getSaveButtonId();
    saveButtonState.setValue(false);
    safeClickById(saveButtonId);
    @Nullable Boolean saveButtonWasClicked = saveButtonState.getValue();
    Assert.assertEquals("Click Save button should execute saveButtonHandler", true,
        saveButtonWasClicked);
  }

  @Test
  public void clickCancel_ShouldCallCancelButtonHandler() {
    @NotNull View<Student> view = getObjectUnderTest();
    @NotNull String cancelButtonId = view.getCancelButtonId();
    cancelButtonState.setValue(false);
    safeClickById(cancelButtonId);
    @Nullable Boolean cancelButtonWasClicked = cancelButtonState.getValue();
    Assert.assertEquals("Click Cancel button should execute cancelButtonHandler", true,
        cancelButtonWasClicked);
  }

  @Test
  public void inputIntoIdField_ShouldSetIdValue() {
    @NotNull View<Student> view = getObjectUnderTest();
    @NotNull String fieldId = view.getFieldId("id");
    int idValue = 1238;
    @NotNull String idValueString = Integer.toString(idValue);
    emptyField(fieldId);
    safeClickById(fieldId).type(idValueString);
    int studentIdValue = student.getId();
    Assert.assertEquals("input into 'id' field should set value of 'id'", idValue,
        studentIdValue);
  }

  @Test
  public void inputIntoNameField_ShouldSetNameValue() {
    @NotNull View<Student> view = getObjectUnderTest();
    @NotNull String fieldId = view.getFieldId("name");
    @NotNull String nameValue = "Rupert Murdock";
    emptyField(fieldId);
    safeClickById(fieldId).type(nameValue);
    @NotNull String studentNameValue = student.getName();
    Assert.assertEquals("input into 'name' field should set value of 'name'", nameValue,
        studentNameValue);
  }

  @Test
  public void inputIntoGpaField_ShouldSetGpaValue() {
    @NotNull View<Student> view = getObjectUnderTest();
    @NotNull String fieldId = view.getFieldId("gpa");
    double gpaValue = 2.14;
    @Nullable String gpaValueString = Double.toString(gpaValue);
    if (gpaValueString == null) {
      throw new RuntimeException("gpaValueString is null");
    }
    emptyField(fieldId);
    safeClickById(fieldId).type(gpaValueString);
    double studentGpaValue = student.getGpa();
    Assert.assertEquals("input into 'gpa' field should set value of 'gpa'", gpaValue,
        studentGpaValue, DOUBLE_PRECISION);
  }

  @Test
  public void inputIntoPenaltyField_ShouldSetPenaltyValue() {
    @NotNull View<Student> view = getObjectUnderTest();
    @NotNull String fieldId = view.getFieldId("penalty");
    short penaltyValue = -12;
    @NotNull String penaltyValueString = Integer.toString(penaltyValue);
    emptyField(fieldId);
    safeClickById(fieldId).type(penaltyValueString);
    int studentPenaltyValue = student.getPenalty();
    Assert.assertEquals("input into 'penalty' field should set value of 'penalty'",
        penaltyValue, studentPenaltyValue);
  }

  @Test
  public void clickOntoGraduatedField_ShouldSetGraduatedValue() {
    @NotNull View<Student> view = getObjectUnderTest();
    @NotNull String fieldId = view.getFieldId("graduated");
    boolean graduatedValue = !student.isGraduated();
    safeClickById(fieldId);
    boolean studentGraduatedValue = student.isGraduated();
    Assert.assertEquals("input into 'id' field should set value of 'id'",
        studentGraduatedValue, graduatedValue);
  }

  @Test
  public void inputIntoLecturesTimeField_ShouldSetLecturesTimeValue() {
    @NotNull View<Student> view = getObjectUnderTest();
    @NotNull String fieldId = view.getFieldId("lecturesTime");
    int lecturesTimeValue = 81472;
    @NotNull String lecturesTimeValueString =
        getDurationFieldExpectedText(lecturesTimeValue, false);
    emptyField(fieldId);
    safeClickById(fieldId).type(lecturesTimeValueString);
    int studentLecturesTimeValue = student.getLecturesTime();
    Assert.assertEquals("input into 'lecturesTime' field should set value of " +
        "'lecturesTime'", lecturesTimeValue, studentLecturesTimeValue);
  }

  @Test
  public void enterKey_ShouldCallSaveButtonHandlerWithTrueParameter() {
    @NotNull View<Student> view = getObjectUnderTest();
    saveButtonState.setValue(false);
    push(KeyCode.ENTER);
    @Nullable Boolean saveButtonWasClicked = saveButtonState.getValue();
    Assert.assertEquals("enter key button should execute saveButtonHandler with " +
        "true parameter", true, saveButtonWasClicked);
  }

  @Test
  public void escapeKey_ShouldCallCancelButtonHandler() {
    @NotNull View<Student> view = getObjectUnderTest();
    cancelButtonState.setValue(false);
    push(KeyCode.ESCAPE);
    @Nullable Boolean cancelButtonWasClicked = cancelButtonState.getValue();
    Assert.assertEquals("escape key button should execute cancelButtonHandler", true,
        cancelButtonWasClicked);
  }

  @NotNull
  @Override
  protected Parent getRootNode() {
    @NotNull JFXView<Student> view = new JFXView<>(viewInfo);
    setObjectUnderTest(view);
    view.setSaveButtonHandler(saveButtonHandler);
    view.setCancelButtonHandler(cancelButtonHandler);
    @Nullable GridPane result = view.getView(student, true);
    return (result != null) ? result : new GridPane();
  }

  @NotNull
  private ViewInfo<Student> prepareViewInfo() {
    @NotNull ViewInfo<Student> result = new ViewInfoImpl<>(Student.class);
    result.addFieldInfo("id", new NaturalFieldInfo<>(Integer.class, "id", 10));
    result.addFieldInfo("name", new TextFieldInfo("name", 100));
    result.addFieldInfo("gpa", new RealFieldInfo<>(Double.class, "gpa", 5));
    result.addFieldInfo("penalty", new IntegerFieldInfo<>(Short.class, "penalty", 5));
    result.addFieldInfo("graduated", new CheckBoxInfo("graduated"));
    result.addFieldInfo("lecturesTime", new DurationFieldInfo("lecturesTime", 2));
    return result;
  }

  @NotNull
  private Student prepareStudent() {
    @NotNull Student result = new Student();
    result.setId(3);
    result.setName("Matthew Libby");
    result.setGpa(2.13);
    result.setPenalty((short) -100);
    result.setGraduated(true);
    result.setLecturesTime(91378);
    return result;
  }
}
