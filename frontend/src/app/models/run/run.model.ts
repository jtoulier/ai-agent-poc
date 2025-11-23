export interface Run {
  id: string;
  status: 'queued' | 'in_progress' | 'completed' | 'failed';
  created_at: string;
  completed_at?: string;
  usage?: {
    prompt_tokens: number;
    completion_tokens: number;
    total_tokens: number;
  };
}