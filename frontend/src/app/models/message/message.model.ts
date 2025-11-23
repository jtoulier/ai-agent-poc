export interface Message {
    type: 'text';
    text: {
        value: string;
        annotations: any[];
    };
}

export interface MessageRequest {
  role: 'user' | 'assistant';
  content: {
    type: 'text';
    text: string;
  }[];
}

export interface MessageResponse {
  id: string;
  object: 'thread.message';
  created_at: number;
  assistant_id?: string;
  thread_id: string;
  run_id?: string;
  role: 'assistant' | 'user';
  content: Message[];
  attachments: any[];
  metadata?: Record<string, any>;
}