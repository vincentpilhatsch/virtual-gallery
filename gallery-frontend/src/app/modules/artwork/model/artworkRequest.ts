export interface ArtworkRequest {
  id?: number,
  description: string,
  requestTime?: number[], // TODO: Change to Data and transfer from number[] when recieving Data
  userId: string,
  artworkId: number
}
