package kr.mintech.weather.api;

import kr.mintech.weather.common.PlanetXSDKConstants.HttpMethod;
import kr.mintech.weather.common.PlanetXSDKException;
import kr.mintech.weather.common.RequestBundle;
import kr.mintech.weather.common.RequestListener;
import kr.mintech.weather.common.ResponseMessage;

/***
 * 
 * @author lhjung
 * @since 2012.05.25
 * 
 */
interface APIRequestInterface {

	public ResponseMessage request(RequestBundle bundle) throws PlanetXSDKException;

	public ResponseMessage request(RequestBundle bundle, HttpMethod httpMethod) throws PlanetXSDKException;
	
	public ResponseMessage request(RequestBundle bundle, String url, HttpMethod httpMethod) throws PlanetXSDKException;

	public void request(RequestBundle bundle, RequestListener requestListener) throws PlanetXSDKException ;
	public void request(RequestBundle bundle, HttpMethod httpMethod, RequestListener requestListener) throws PlanetXSDKException ;
	public void request(RequestBundle bundle, String url, HttpMethod httpMethod, RequestListener requestListener) throws PlanetXSDKException ;

	
}
