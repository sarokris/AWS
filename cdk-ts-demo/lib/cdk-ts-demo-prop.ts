import { Duration } from "aws-cdk-lib"
import { HttpMethod } from "aws-cdk-lib/aws-events"

export interface DemoApiProps {
    lambdaFunctions: LambdaListType[]
}

export type LambdaListType = {
    fuctionName: string,
    fuctionId: string,
    handler: string,
    path?: string,
    method: HttpMethod,
    timeOut: Duration     
}