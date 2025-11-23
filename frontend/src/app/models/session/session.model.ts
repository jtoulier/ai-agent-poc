import { MessageResponse, Message } from '@app/models/message';
import { Thread } from '@app/models/thread/thread.model';

export interface Session {
  fullName: string;
  thread: Thread | null;
  messages: MessageResponse[];
  lastMessage: Message | null;
}