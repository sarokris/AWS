export async function handler(event:string, context: string) {
    console.log('event : ' +  event);
    return {
        statusCode: 200,
        body: 'Health is OK' 
    }

}