export interface Event {
  id: number,
  title: string,
  description: string,
  location: string,
  dateTime: number[],
  image: string,
  confirmedUserIds: string[],
}
