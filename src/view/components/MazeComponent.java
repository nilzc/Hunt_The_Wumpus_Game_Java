package view.components;

import javax.swing.JComponent;

import controller.Features;

/**
 * Maze view component interface.
 */
public interface MazeComponent {

  /**
   * Add listener.
   *
   * @param features the controller
   */
  void addListener(Features features);

  /**
   * Get the main component.
   *
   * @return the main component
   */
  JComponent getMainComponent();

  /**
   * Set the main component as visible.
   */
  void setVisible();

  /**
   * Set the main component as invisible.
   */
  void setInvisible();

}
