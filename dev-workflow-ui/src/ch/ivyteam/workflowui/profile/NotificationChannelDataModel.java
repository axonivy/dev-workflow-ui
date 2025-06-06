package ch.ivyteam.workflowui.profile;

import java.util.List;
import java.util.Locale;

import ch.ivyteam.ivy.notification.channel.Event;
import ch.ivyteam.ivy.notification.channel.NotificationSubscription;
import ch.ivyteam.ivy.security.ISecurityContext;
import ch.ivyteam.ivy.security.ISecurityMember;

public class NotificationChannelDataModel {

  private final ISecurityMember subscriber;
  private final ISecurityContext securityContext;

  private List<NotificationEventDto> events;
  private List<NotificationChannelDto> channels;

  private NotificationChannelDataModel(ISecurityMember subscriber) {
    this.subscriber = subscriber;
    this.securityContext = subscriber.getSecurityContext();
  }

  public void onload() {
    events = NotificationEventDto.all();
    channels = NotificationChannelDto.all(subscriber, securityContext);
  }

  public void save() {
    channels.forEach(this::saveChannel);
  }

  private void saveChannel(NotificationChannelDto channel) {
    channel.getSubscriptions().entrySet().forEach(eventSubscription -> {
      var subscription = NotificationSubscription.of(subscriber, channel.getChannel(), eventSubscription.getKey());
      subscription.state(eventSubscription.getValue().getState().toDbState());
    });
  }

  public List<NotificationChannelDto> getChannels() {
    return channels;
  }

  public List<NotificationEventDto> getEvents() {
    return events;
  }

  public static NotificationChannelDataModel instance(ISecurityMember member) {
    var model = new NotificationChannelDataModel(member);
    model.onload();
    return model;
  }

  public static final class NotificationEventDto {

    private final Event event;

    private NotificationEventDto(Event event) {
      this.event = event;
    }

    public Event getEvent() {
      return event;
    }

    public String getDisplayName() {
      return event.displayName(Locale.ENGLISH);
    }

    public String getDescription() {
      return event.description(Locale.ENGLISH);
    }

    public static List<NotificationEventDto> all() {
      return Event.all().stream().map(NotificationEventDto::new).toList();
    }

    public static NotificationEventDto of(Event event) {
      return new NotificationEventDto(event);
    }
  }
}
