package application.state;

public interface State {
    public void numberPressed(int number);
    public void cancelPressed();
    public void backPressed();
    public void acceptPressed();
    public void optionTopLeft();
    public void optionBottomLeft();
    public void optionTopRight();
    public void optionBottomRight();
}
