package ckSnapInterpreter;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.TimelineBuilder;
import javafx.scene.Node;
import javafx.util.Duration;

public class BounceTransition extends CachedTimelineTransition {
    /**
     * Create new BounceOutLeftTransition
     * 
     * @param node The node to affect
     */
    public BounceTransition(final Node node) {
        super(
                node,
                TimelineBuilder.create()
                    .keyFrames(
                        new KeyFrame(Duration.millis(0), new KeyValue(node.translateYProperty(), 0, WEB_EASE)),
                        new KeyFrame(Duration.millis(200), new KeyValue(node.translateYProperty(), 0, WEB_EASE)),
                        new KeyFrame(Duration.millis(400), new KeyValue(node.translateYProperty(), -0.30*node.getBoundsInParent().getHeight(), WEB_EASE)),
                        new KeyFrame(Duration.millis(500), new KeyValue(node.translateYProperty(), 0, WEB_EASE)),
                        new KeyFrame(Duration.millis(600), new KeyValue(node.translateYProperty(), -0.15*node.getBoundsInParent().getHeight(), WEB_EASE)),
                        new KeyFrame(Duration.millis(800), new KeyValue(node.translateYProperty(), 0, WEB_EASE)),
                        new KeyFrame(Duration.millis(1000), new KeyValue(node.translateYProperty(), 0, WEB_EASE))
                    )
                    .build()
                );
            setCycleDuration(Duration.seconds(1));
            setDelay(Duration.seconds(0.2));
        }
    }